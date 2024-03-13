package org.bigant.fw.lark;

import com.alibaba.fastjson2.JSONObject;
import com.lark.oapi.core.utils.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.bigant.wf.exception.WfException;
import org.bigant.wf.instances.InstancesAction;
import org.bigant.wf.instances.InstanceStatus;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * 飞书回调
 *
 * @author galen
 * @date 2024/2/2813:50
 */
@Slf4j
public class LarkCallback {

    public static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private InstancesAction action;

    private byte[] keyBs;

    private String encryptKey;

    private String verificationToken;


    public LarkCallback(InstancesAction action, String verificationToken, String encryptKey) {
        this.action = action;
        this.verificationToken = verificationToken;
        this.encryptKey = encryptKey;

        if (Strings.isNotEmpty(encryptKey)) {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                // won't happen
            }
            if (digest != null) {
                keyBs = digest.digest(encryptKey.getBytes(StandardCharsets.UTF_8));
            }
        }


    }

    public String callback(String timestamp, String nonce, String sign, String bodyString) throws Exception {
        JSONObject body = JSONObject.parseObject(bodyString);

        String decrypt = this.decrypt(body.getString("encrypt"));

        JSONObject data = JSONObject.parseObject(decrypt);
        if (data.getString("type").equals("url_verification")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("challenge", data.getString("challenge"));
            return jsonObject.toString();
        }

        if (!verifySign(timestamp, nonce, sign, bodyString)) {
            throw new WfException("飞书-回调签名验证失败。");
        }

        this.callback(data);
        return "success";
    }

    public void callback(JSONObject body) {
        if (Strings.isNotEmpty(verificationToken)) {
            if (!verificationToken.equals(body.getString("token"))) {
                throw new WfException("飞书-接收回调签名校验失败");
            }
        }

        String type = body.getString("type");

        if ("event_callback".equals(type)) {
            this.instancesCallback(body.getJSONObject("event"));
        }
    }


    public void instancesCallback(JSONObject event) {

        /*PENDING - 审批中
        APPROVED - 已通过
        REJECTED - 已拒绝
        CANCELED - 已撤回
        DELETED - 已删除
        REVERTED - 已撤销
        OVERTIME_CLOSE - 超时被关闭
        OVERTIME_RECOVER - 超时实例被恢复 */
        Long operateTimeSecond = event.getLong("instance_operate_time");

        //时间戳转LocalDateTime
        LocalDateTime operateTime = DateUtil.timestampToLocalDateTime(operateTimeSecond);


        InstancesAction.InstancesCallback instancesCallback = InstancesAction.InstancesCallback.builder()
                .instanceCode(event.getString("instance_code"))
                .processCode(event.getString("approval_code"))
                .operateTime(operateTime)
                .build();

        switch (event.getString("status")) {
            case "PENDING":
                instancesCallback.setAction(InstanceStatus.RUNNING);
                break;
            case "APPROVED":
                instancesCallback.setAction(InstanceStatus.AGREED);
                break;
            case "REJECTED":
                instancesCallback.setAction(InstanceStatus.REFUSED);
                break;
            case "CANCELED":
            case "REVERTED":
                instancesCallback.setAction(InstanceStatus.CANCELED);
            case "DELETED":
                instancesCallback.setAction(InstanceStatus.DELETED);
                break;
            case "OVERTIME_CLOSE":
            case "OVERTIME_RECOVER":
                //暂时不处理两个关闭状态
                return;
            default:
                throw new WfException("飞书-无法识别的实例状态:" + event.getString("status"));
        }

        action.action(instancesCallback);

    }

    private boolean verifySign(String timestamp, String nonce, String sourceSign, String bodyString) throws NoSuchAlgorithmException {
        if (Strings.isEmpty(encryptKey)) {
            return true;
        }
        String targetSign;
        targetSign = calculateSignature(timestamp, nonce, encryptKey, bodyString);
        return targetSign.equals(sourceSign);
    }

    protected String calculateSignature(String timestamp, String nonce, String encryptKey, String bodyString) throws NoSuchAlgorithmException {
        String content = timestamp + nonce + encryptKey + bodyString;
        MessageDigest alg = MessageDigest.getInstance("SHA-256");
        return Hex.encodeHexString(alg.digest(content.getBytes()));
    }

    public void callback(String body) {
        this.callback(JSONObject.parseObject(body));
    }


    public String decrypt(String base64) throws Exception {
        byte[] decode = Base64.getDecoder().decode(base64);
        Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
        byte[] iv = new byte[16];
        System.arraycopy(decode, 0, iv, 0, 16);
        byte[] data = new byte[decode.length - 16];
        System.arraycopy(decode, 16, data, 0, data.length);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBs, "AES"), new IvParameterSpec(iv));
        byte[] r = cipher.doFinal(data);
        if (r.length > 0) {
            int p = r.length - 1;
            for (; p >= 0 && r[p] <= 16; p--) {
            }
            if (p != r.length - 1) {
                byte[] rr = new byte[p + 1];
                System.arraycopy(r, 0, rr, 0, p + 1);
                r = rr;
            }
        }
        return new String(r, StandardCharsets.UTF_8);
    }
}
