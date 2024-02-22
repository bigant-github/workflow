package org.bigant.fw.lark;

import com.lark.oapi.Client;
import com.lark.oapi.core.enums.BaseUrlEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * 飞书配置
 *
 * @author galen
 * @date 2024/2/1816:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LarkConfig {

    @ApiModelProperty("应用id")
    private String appId;
    @ApiModelProperty("应用密钥")
    private String appSecret;

    private boolean logDebug = false;

    private BaseUrlEnum openBaseUrl = BaseUrlEnum.FeiShu;

    private int requestTimeout = 3;

    private Client client;

    public LarkConfig(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
    }

    public synchronized Client getClient() {
        if (client == null) {
            this.client = Client.newBuilder(appId, appSecret)
                    .openBaseUrl(openBaseUrl) // 设置域名，默认为飞书
                    .requestTimeout(requestTimeout, TimeUnit.SECONDS) // 设置httpclient 超时时间，默认永不超时
                    .logReqAtDebug(logDebug) // 在 debug 模式下会打印 http 请求和响应的 headers,body 等信息。
                    .build();
        }
        return client;
    }
}

