package org.bigant.wf.lark.springboot;

import com.lark.oapi.Client;
import com.lark.oapi.core.enums.BaseUrlEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * 飞书spring配置文件
 *
 * @author galen
 * @date 2024/2/2816:29
 */
@Data
@ConfigurationProperties(prefix = "wf.lark")
public class LarkProperties {
    private String appId;
    private String appSecret;
    private boolean logDebug = false;
    private BaseUrlEnum openBaseUrl = BaseUrlEnum.FeiShu;
    private int requestTimeout = 3;

    private CallbackProperties callback;


    public Client client() {
        return Client.newBuilder(appId, appSecret)
                .openBaseUrl(openBaseUrl) // 设置域名，默认为飞书
                .requestTimeout(requestTimeout, TimeUnit.SECONDS) // 设置httpclient 超时时间，默认永不超时
                .logReqAtDebug(logDebug) // 在 debug 模式下会打印 http 请求和响应的 headers,body 等信息。
                .build();
    }

    @Data
    public static class CallbackProperties {
        private Boolean enabled = false;
        private String type = "http";
        private String httpPath = "lark/callback";
        private String verificationToken;
        private String encryptKey;
    }
}
