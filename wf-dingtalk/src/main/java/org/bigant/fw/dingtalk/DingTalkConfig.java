package org.bigant.fw.dingtalk;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigant.wf.cache.ICache;
import org.bigant.wf.exception.WfException;

import java.util.concurrent.TimeUnit;

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
    public String token;
    public String aesKey;
    private ICache cache;

    private Client client;

    private static final String ACCESS_TOKEN_CACHE_KEY = DingTalkConstant.CACHE_KEY+"access_token:";




    /**
     * 获取访问 token
     */
    public String accessToken() {
        String accessToken = cache.get(ACCESS_TOKEN_CACHE_KEY + appKey);

        if (accessToken != null) {
            return accessToken;
        }

        Client client = getClient();
        GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest()
                .setAppKey(appKey)
                .setAppSecret(appSecret);

        try {
            accessToken = client.getAccessToken(getAccessTokenRequest).getBody().getAccessToken();
            cache.set(ACCESS_TOKEN_CACHE_KEY + appKey, accessToken, 7100, TimeUnit.SECONDS);
            return accessToken;
        } catch (Exception _err) {
            throw new WfException("钉钉-获取accessToken失败", _err);
        }
    }
}
