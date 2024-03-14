package org.bigant.fw.dingtalk;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dingtalk.oapi.lib.aes.DingTalkEncryptor;
import com.dingtalk.oapi.lib.aes.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigant.wf.exception.WfException;
import org.bigant.wf.instances.InstancesAction;
import org.bigant.wf.instances.InstanceStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

/**
 * 飞书回调
 *
 * @author galen
 * @date 2024/2/2813:50
 */

@AllArgsConstructor
@Slf4j
public class DingTalkCallback {

    public static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private InstancesAction action;
    private String token;
    private String aesKey;

    public Map<String, String> callback(String signature,
                                        String timestamp,
                                        String nonce,
                                        String appKey,
                                        String body) {
        String params = " signature:" + signature + " timestamp:" + timestamp + " nonce:" + nonce + " json:" + body;
        try {

            DingTalkEncryptor dingTalkEncryptor = new DingTalkEncryptor(token, aesKey, appKey);

            JSONObject bodyJson = JSONObject.parseObject(body);
            //从post请求的body中获取回调信息的加密数据进行解密处理
            String encryptMsg = bodyJson.getString("encrypt");
            String plainText = dingTalkEncryptor.getDecryptMsg(signature, timestamp, nonce, encryptMsg);
            JSONObject obj = JSON.parseObject(plainText);
            this.callback(obj);
            // 返回success的加密信息表示回调处理成功
            return dingTalkEncryptor.getEncryptedMap("success", System.currentTimeMillis(), Utils.getRandomStr(8));
        } catch (Exception e) {
            //失败的情况，应用的开发者应该通过告警感知，并干预修复
            log.error("钉钉-回调失败：" + params, e);
            return null;
        }

    }


    public void callback(JSONObject body) {
        String type = body.getString("EventType");
        if ("bpms_instance_change".equals(type)) {
            this.instancesCallback(body);
        }
    }


    public void instancesCallback(JSONObject event) {

        /*
        start：审批实例开始
        finish：审批正常结束（同意或拒绝）
        terminate：审批终止（发起人撤销审批单） */

        LocalDateTime operateTime;

        Long operateTimeSecond = event.getLong("EventTime");
        if (operateTimeSecond != null) {
            //时间戳转LocalDateTime
            operateTimeSecond = operateTimeSecond * 1000;

            Instant instant = Instant.ofEpochMilli(operateTimeSecond);
            operateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        } else {
            operateTime = LocalDateTime.now();
        }


        InstancesAction.InstancesCallback instancesCallback = InstancesAction.InstancesCallback.builder()
                .instanceCode(event.getString("processInstanceId"))
                .processCode(event.getString("processCode"))
                .channelName(DingTalkConstant.NAME)
                .operateTime(operateTime)
                .build();

        switch (event.getString("type")) {
            case "start":
                instancesCallback.setAction(InstanceStatus.RUNNING);
                break;
            case "finish":
                switch (event.getString("result")) {
                    case "agree":
                        instancesCallback.setAction(InstanceStatus.AGREED);
                        break;
                    case "refuse":
                        instancesCallback.setAction(InstanceStatus.REFUSED);
                        break;
                    default:
                        throw new WfException("钉钉-无法识别的实例状态:" + event.getString("result"));
                }
                break;
            case "terminate":
                instancesCallback.setAction(InstanceStatus.CANCELED);
                this.action.canceled(instancesCallback);
                break;
            default:
                throw new WfException("钉钉-无法识别的实例状态:" + event.getString("type"));
        }
        this.action.action(instancesCallback);

    }

}
