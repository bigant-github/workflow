package org.bigant.wf.lark.springboot;

import org.bigant.fw.lark.LarkConfig;
import org.bigant.fw.lark.instances.LarkInstancesService;
import org.bigant.fw.lark.process.LarkProcessService;
import org.bigant.wf.user.UserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
public class WfLarkSpringBootAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("wf.lark")
    public LarkConfig larkConfig() {
        return new LarkConfig();
    }


    @Bean
    @ConditionalOnMissingBean
    public LarkProcessService larkProcessService(LarkConfig larkConfig) {
        return new LarkProcessService(larkConfig);
    }

    @Bean
    @ConditionalOnMissingBean
    public LarkInstancesService larkInstancesService(LarkConfig larkConfig, LarkProcessService larkProcessService, UserService userService) {
        return new LarkInstancesService(larkConfig, larkProcessService, userService);
    }


}
