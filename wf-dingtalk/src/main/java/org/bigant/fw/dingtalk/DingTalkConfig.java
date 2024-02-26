package org.bigant.fw.dingtalk;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 钉钉配置
 *
 * @author galen
 * @date 2024/1/30 15:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class DingTalkConfig {

    @ApiModelProperty("应用key")
    private String appKey;
    @ApiModelProperty("应用密钥")
    private String appSecret;
    @ApiModelProperty("管理员用户id")
    private String managerUserId;
    @ApiModelProperty("应用id")
    private Long agentId;


    private String accessToken;
    private long accessTokenExpiresIn = 0;
    private long accessTokenTimeOut = 7100 * 1000;

    public DingTalkConfig(String appKey, String appSecret, String managerUserId, Long agentId) {
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.managerUserId = managerUserId;
        this.agentId = agentId;
    }

    /**
     * 使用 Token 初始化账号Client
     *
     * @return Client
     * @throws Exception
     */
    public static Client createClient() {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        try {
            return new Client(config);
        } catch (Exception e) {
            log.error("钉钉创建Client失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取访问 token
     *
     * @throws Exception 异常信息
     */
    public String accessToken() {
        long t = System.currentTimeMillis();

        if (t < accessTokenExpiresIn) {
            return accessToken;
        }

        Client client = createClient();
        GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest()
                .setAppKey(appKey)
                .setAppSecret(appSecret);
        try {
            this.accessToken = client.getAccessToken(getAccessTokenRequest).getBody().getAccessToken();
            this.accessTokenExpiresIn = t + accessTokenTimeOut;
            return this.accessToken;
        } catch (TeaException err) {
            throw err;
        } catch (Exception _err) {
            throw new TeaException(_err.getMessage(), _err);
        }
    }
}
