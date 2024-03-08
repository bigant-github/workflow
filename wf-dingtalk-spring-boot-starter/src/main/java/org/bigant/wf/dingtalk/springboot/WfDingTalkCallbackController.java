package org.bigant.wf.dingtalk.springboot;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.dingtalk.DingTalkCallback;
import org.bigant.fw.dingtalk.DingTalkConfig;
import org.bigant.wf.instances.InstancesAction;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 回调接口
 *
 * @author galen
 * @date 2024/2/2816:41
 */
@ConditionalOnProperty(value = "wf.dingtalk.callback.enable", havingValue = "true")
@ConditionalOnBean(value = InstancesAction.class)
@RestController
@Slf4j
@AllArgsConstructor
public class WfDingTalkCallbackController {

    private final DingTalkCallback dingTalkCallback;
    private final DingTalkConfig dingTalkConfig;

    /**
     * 回调接口
     */
    @PostMapping("${wf.dingtalk.callback.path:dingtalk/callback}")
    public Map<String, String> callback(@RequestParam(value = "signature", required = false) String signature,
                                        @RequestParam(value = "timestamp", required = false) String timestamp,
                                        @RequestParam(value = "nonce", required = false) String nonce,
                                        @RequestBody(required = false) String body) {
        String params = " signature:" + signature + " timestamp:" + timestamp + " nonce:" + nonce + " json:" + body;
        log.info("钉钉-接收到回调:{}", params);
        return dingTalkCallback.callback(signature,
                timestamp,
                nonce,
                dingTalkConfig.getToken(),
                dingTalkConfig.getAesKey(),
                dingTalkConfig.getAppKey(),
                body);
    }
}