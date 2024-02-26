package org.bigant.wf.dingtalk.springboot;

import org.bigant.fw.dingtalk.DingTalkConfig;
import org.bigant.fw.dingtalk.instances.DingTalkInstancesService;
import org.bigant.fw.dingtalk.process.DingTalkProcessService;
import org.bigant.wf.user.UserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * spring boot 自动配置
 *
 * @author galen
 * @date 2024/2/2316:42
 */
@Configuration(proxyBeanMethods = false)
public class WfDingTalkSpringBootAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("wf.dingtalk")
    public DingTalkConfig dingTalkConfig() {
        return new DingTalkConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public DingTalkInstancesService dingTalkInstancesService(DingTalkConfig dingTalkConfig, UserService userService) {
        return new DingTalkInstancesService(dingTalkConfig, userService);
    }

    @Bean
    @ConditionalOnMissingBean
    public DingTalkProcessService dingTalkProcessService(DingTalkConfig dingTalkConfig, UserService userService) {
        return new DingTalkProcessService(dingTalkConfig, userService);
    }

}