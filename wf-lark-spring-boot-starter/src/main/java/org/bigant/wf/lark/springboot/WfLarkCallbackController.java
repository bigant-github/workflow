package org.bigant.wf.lark.springboot;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.lark.LarkCallback;
import org.bigant.fw.lark.LarkConfig;
import org.bigant.wf.instances.InstancesAction;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
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
/*@ConditionalOnProperty(value = "wf.lark.callback.enable", havingValue = "true")*/
@ConditionalOnExpression(value = "${wf.lark.callback.enable:true} and 'http'.equals('${wf.lark.callback.type}')")
@ConditionalOnBean(value = InstancesAction.class)
@RestController
@Slf4j
@AllArgsConstructor
public class WfLarkCallbackController {

    private final LarkCallback larkCallback;

    /**
     * 回调接口
     */
    @PostMapping("${wf.lark.callback.path:lark/callback}")
    public String callback(@RequestHeader(value = "X-Lark-Request-Timestamp", required = false) String timestamp,
                           @RequestHeader(value = "X-Lark-Request-Nonce", required = false) String nonce,
                           @RequestHeader(value = "X-Lark-Signature", required = false) String sign,
                           @RequestBody String body) throws Exception {

        return larkCallback.callback(timestamp, nonce, sign, body);
    }

}
