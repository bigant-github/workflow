package org.bigant.fw.lark;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import org.bigant.wf.exception.WfException;
import org.bigant.wf.instances.InstancesAction;
import org.bigant.wf.instances.InstancesStatus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 飞书回调
 *
 * @author galen
 * @date 2024/2/2813:50
 */

@AllArgsConstructor
public class LarkCallback {

    public static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private InstancesAction action;

    public void callback(String timestamp, String nonce, String encryptKey, String sign, String bodyString) throws NoSuchAlgorithmException {
        String signature = this.calculateSignature(timestamp, nonce, encryptKey, bodyString);
        if (!signature.equals(sign)) {
            throw new WfException("飞书-接收回调签名校验失败");
        }

    }

    public void callback(String body) {
        this.callback(JSONObject.parseObject(body));

    }

    public void callback(JSONObject body) {
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
        operateTimeSecond = operateTimeSecond * 1000;

        Instant instant = Instant.ofEpochMilli(operateTimeSecond);
        LocalDateTime operateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());


        InstancesAction.InstancesCallback instancesCallback = InstancesAction.InstancesCallback.builder()
                .instanceCode(event.getString("instance_code"))
                .processCode(event.getString("approval_code"))
                .operateTime(operateTime)
                .build();

        switch (event.getString("status")) {
            case "PENDING":
                instancesCallback.setAction(InstancesStatus.RUNNING);
                break;
            case "APPROVED":
                instancesCallback.setAction(InstancesStatus.AGREED);
                break;
            case "REJECTED":
                instancesCallback.setAction(InstancesStatus.REFUSED);
                break;
            case "CANCELED":
            case "REVERTED":
                instancesCallback.setAction(InstancesStatus.CANCELED);
            case "DELETED":
                instancesCallback.setAction(InstancesStatus.DELETED);
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


}
