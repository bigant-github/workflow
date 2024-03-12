package org.bigant.fw.lark;

import com.alibaba.fastjson2.JSONObject;
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

public class LarkCallback {

    public static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private InstancesAction action;

    private byte[] keyBs;

    private boolean hasEncryptKey;
    private String encryptKey;

    private boolean hasVerificationToken;
    private String verificationToken;


    public LarkCallback(InstancesAction action, String verificationToken, String encryptKey) {
        this.action = action;
        this.verificationToken = verificationToken;
        this.encryptKey = encryptKey;

        hasEncryptKey = encryptKey != null && !encryptKey.isEmpty();
        hasVerificationToken = verificationToken != null && !verificationToken.isEmpty();
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

    public void callback(String timestamp, String nonce, String sign, String bodyString) throws Exception {

        if (hasEncryptKey) {
            String signature = this.calculateSignature(timestamp, nonce, encryptKey, bodyString);
            if (!signature.equals(sign)) {
                throw new WfException("飞书-接收回调签名校验失败");
            }
        }

        this.callback(this.decrypt(bodyString));
    }

    public void callback(String body) {
        this.callback(JSONObject.parseObject(body));
    }

    public void callback(JSONObject body) {
        if (hasVerificationToken) {
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


    public String calculateSignature(String timestamp, String nonce, String encryptKey, String bodyString)
            throws NoSuchAlgorithmException {
        String content = timestamp + nonce + encryptKey + bodyString;
        MessageDigest alg = MessageDigest.getInstance("SHA-256");
        return this.encodeHexString(alg.digest(content.getBytes()));
    }

    private String encodeHexString(byte[] data) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_UPPER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_UPPER[0x0F & data[i]];
        }
        return new String(out);
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
