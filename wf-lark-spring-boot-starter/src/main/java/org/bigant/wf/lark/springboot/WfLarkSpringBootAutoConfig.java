package org.bigant.wf.lark.springboot;

import com.lark.oapi.Client;
import org.bigant.fw.lark.LarkCallback;
import org.bigant.fw.lark.LarkConfig;
import org.bigant.fw.lark.LarkFile;
import org.bigant.fw.lark.instances.LarkInstancesService;
import org.bigant.fw.lark.instances.form.LarkFDCF;
import org.bigant.fw.lark.process.LarkProcessService;
import org.bigant.wf.Factory;
import org.bigant.wf.cache.ICache;
import org.bigant.wf.cache.LocalCache;
import org.bigant.wf.instances.InstancesAction;
import org.bigant.wf.user.UserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * spring boot 自动配置
 *
 * @author galen
 * @date 2024/2/2316:42
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(LarkProperties.class)
public class WfLarkSpringBootAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public ICache wfCache() {
        return LocalCache.getInstance();
    }

    @Bean
    @ConditionalOnMissingBean
    public LarkConfig larkConfig(LarkProperties larkProperties) {

        LarkConfig larkConfig = new LarkConfig(larkProperties.getAppId()
                , larkProperties.getAppSecret());

        larkConfig.setClient(Client.newBuilder(larkProperties.getAppId(), larkProperties.getAppSecret())
                .openBaseUrl(larkProperties.getOpenBaseUrl()) // 设置域名，默认为飞书
                .requestTimeout(larkProperties.getRequestTimeout(), TimeUnit.SECONDS) // 设置httpclient 超时时间，默认永不超时
                .logReqAtDebug(larkProperties.isLogDebug()) // 在 debug 模式下会打印 http 请求和响应的 headers,body 等信息。
                .build());

        return larkConfig;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(InstancesAction.class)
    @ConditionalOnProperty(value = "wf.lark.callback.enable", havingValue = "true")
    public LarkCallback larkCallback(InstancesAction instancesAction, LarkProperties larkProperties) {
        return new LarkCallback(instancesAction,
                larkProperties.getCallback().getVerificationToken(),
                larkProperties.getCallback().getEncryptKey());
    }

    @Bean
    @ConditionalOnMissingBean
    public LarkFile larkFile(LarkConfig larkConfig) {
        return new LarkFile(larkConfig);
    }


    @Bean
    @ConditionalOnMissingBean
    public LarkProcessService larkProcessService(LarkConfig larkConfig) {
        LarkProcessService larkProcessService = new LarkProcessService(larkConfig);
        Factory.registerProcessService(larkProcessService.getChannelName(), larkProcessService);
        return larkProcessService;
    }

    @Bean
    @ConditionalOnMissingBean
    public LarkFDCF larkFDCF(LarkFile larkFile) {
        return new LarkFDCF(larkFile);
    }

    @Bean
    @ConditionalOnMissingBean
    public LarkInstancesService larkInstancesService(
            LarkProperties larkProperties,
            LarkConfig larkConfig,
            LarkProcessService larkProcessService,
            UserService userService,
            LarkFDCF larkFDCF,
            ICache cache) {

        LarkInstancesService larkInstancesService = new LarkInstancesService(larkConfig,
                larkProcessService,
                userService,
                larkFDCF,
                cache,
                larkProperties.getCallback().getEnabled());
        Factory.registerInstancesService(larkInstancesService.getChannelName(), larkInstancesService);
        return larkInstancesService;
    }


}
