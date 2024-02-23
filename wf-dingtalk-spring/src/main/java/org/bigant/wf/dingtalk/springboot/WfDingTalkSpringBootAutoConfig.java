package org.bigant.wf.dingtalk.springboot;

import org.bigant.fw.dingtalk.DingTalkConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * spring boot 自动配置
 * @author galen
 * @date 2024/2/2316:42
 */
@Configuration(proxyBeanMethods = false)
public class WfDingTalkSpringBootAutoConfig {
    @Bean
    @ConditionalOnMissingBean
    public DingTalkConfig freeMarkerLanguageDriver() {
        return new MybatisFreeMarkerLanguageDriver(config);
    }
}
