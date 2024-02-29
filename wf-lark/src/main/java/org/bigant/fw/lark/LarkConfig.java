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

    @ApiModelProperty("应用密钥")
    private String encryptKey;

    private boolean logDebug = false;

    private BaseUrlEnum openBaseUrl = BaseUrlEnum.FeiShu;

    private int requestTimeout = 3;

    private Client client;

    public LarkConfig(String appId, String appSecret, String encryptKey) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.encryptKey = encryptKey;
    }

}

