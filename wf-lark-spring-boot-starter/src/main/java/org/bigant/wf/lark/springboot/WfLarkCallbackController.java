package org.bigant.wf.lark.springboot;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.lark.LarkCallback;
import org.bigant.fw.lark.LarkConfig;
import org.bigant.wf.instances.InstancesAction;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;

/**
 * 回调接口
 *
 * @author galen
 * @date 2024/2/2816:41
 */
@ConditionalOnProperty(value = "wf.lark.callback.enable", havingValue = "true")
@ConditionalOnBean(value = InstancesAction.class)
@RestController
@Slf4j
@AllArgsConstructor
public class WfLarkCallbackController {

    private final LarkCallback larkCallback;
    private final LarkConfig larkConfig;

    /**
     * 回调接口
     */
    @PostMapping("${wf.lark.callback.path:lark/callback}")
    public String callback(@RequestHeader("X-Lark-Request-Timestamp") String timestamp,
                           @RequestHeader("X-Lark-Request-Nonce") String nonce,
                           @RequestHeader("X-Lark-Signature") String sign,
                           @RequestBody String body) throws Exception {
        larkCallback.callback(timestamp, nonce, sign, body);
        return "";
    }

}
