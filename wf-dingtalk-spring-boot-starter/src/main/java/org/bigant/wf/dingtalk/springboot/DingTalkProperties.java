package org.bigant.wf.dingtalk.springboot;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.teaopenapi.models.Config;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bigant.wf.exception.WfException;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 飞书spring配置文件
 *
 * @author galen
 * @date 2024/2/2816:29
 */
@Data
@ConfigurationProperties(prefix = "wf.dingtalk")
@Slf4j
public class DingTalkProperties {

    private String appKey;
    private String appSecret;
    private String managerUserId;
    private Long agentId;
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
