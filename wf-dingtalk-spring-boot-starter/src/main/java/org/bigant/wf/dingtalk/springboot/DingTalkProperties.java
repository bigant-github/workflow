package org.bigant.wf.dingtalk.springboot;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 飞书spring配置文件
 *
 * @author galen
 * date 2024/2/2816:29
 */
@Data
@ConfigurationProperties(prefix = "wf.dingtalk")
@Slf4j
public class DingTalkProperties {

    private String appKey;
    private String appSecret;
    private String managerUserId;
    private Long agentId;
    private String corpId;
    private CallbackProperties callback;

    @Data
    public static class CallbackProperties {
        private Boolean enabled = false;
        private String type = "http";
        private String httpPath = "dingtalk/callback";
        private String verificationToken;
        private String encryptKey;
    }

}
