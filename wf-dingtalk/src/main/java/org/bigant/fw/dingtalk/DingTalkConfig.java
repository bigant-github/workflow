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
    public String getAccessToken() {
        Client client = createClient();
        GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest()
                .setAppKey(appKey)
                .setAppSecret(appSecret);
        try {
            return client.getAccessToken(getAccessTokenRequest).getBody().getAccessToken();
        } catch (TeaException err) {
            throw err;
        } catch (Exception _err) {
            throw new TeaException(_err.getMessage(), _err);
        }
    }
}
