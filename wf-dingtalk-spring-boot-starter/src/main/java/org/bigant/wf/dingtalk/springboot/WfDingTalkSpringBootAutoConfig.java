package org.bigant.wf.dingtalk.springboot;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.dingtalk.DingTalkCallback;
import org.bigant.fw.dingtalk.DingTalkConfig;
import org.bigant.fw.dingtalk.DingTalkUser;
import org.bigant.fw.dingtalk.instances.form.DingTalkFDCF;
import org.bigant.fw.dingtalk.instances.DingTalkInstancesService;
import org.bigant.fw.dingtalk.process.DingTalkProcessService;
import org.bigant.fw.dingtalk.process.form.DingTalkFDTCF;
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

/**
 * spring boot 自动配置
 *
 * @author galen
 * date 2024/2/2316:42
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(DingTalkProperties.class)
@Slf4j
public class WfDingTalkSpringBootAutoConfig {


    @Bean
    @ConditionalOnMissingBean
    public ICache wfCache() {
        return LocalCache.getInstance();
    }

    @Bean
    @ConditionalOnMissingBean
    public DingTalkConfig dingTalkConfig(DingTalkProperties dingTalkProperties, ICache cache) throws Exception {
        return new DingTalkConfig(dingTalkProperties.getAppKey(),
                dingTalkProperties.getAppSecret(),
                dingTalkProperties.getManagerUserId(),
                dingTalkProperties.getAgentId(),
                cache,
                new Client(this.getConfig()));
    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "wf.dingtalk.callback.enable", havingValue = "true")
    @ConditionalOnBean(InstancesAction.class)
    public DingTalkCallback dingTalkCallback(InstancesAction instancesAction, DingTalkProperties dingTalkProperties) {
        return new DingTalkCallback(instancesAction,
                dingTalkProperties.getCallback().getVerificationToken(),
                dingTalkProperties.getCallback().getEncryptKey());
    }

    @Bean
    @ConditionalOnMissingBean
    public DingTalkUser dingTalkUser(DingTalkConfig dingTalkConfig, ICache cache) {
        return new DingTalkUser(dingTalkConfig, cache);
    }

    @Bean
    @ConditionalOnMissingBean
    public DingTalkFDCF dingTalkCCF(DingTalkConfig dingTalkConfig, DingTalkUser dingTalkUser, ICache cache) throws Exception {
        return new DingTalkFDCF(dingTalkConfig,
                dingTalkUser,
                cache,
                new com.aliyun.dingtalkworkflow_1_0.Client(getConfig()));
    }

    @Bean
    @ConditionalOnMissingBean
    public DingTalkInstancesService dingTalkInstancesService(DingTalkConfig dingTalkConfig, DingTalkFDCF dingTalkFDCF, UserService userService) throws Exception {
        DingTalkInstancesService dingTalkInstancesService = new DingTalkInstancesService(dingTalkConfig,
                dingTalkFDCF,
                userService,
                new com.aliyun.dingtalkworkflow_1_0.Client(getConfig()));
        //注册到工厂供后续使用
        Factory.registerInstancesService(dingTalkInstancesService.getChannelName(), dingTalkInstancesService);

        return dingTalkInstancesService;
    }

    @Bean
    @ConditionalOnMissingBean
    public DingTalkProcessService dingTalkProcessService(DingTalkConfig dingTalkConfig, UserService userService) throws Exception {

        DingTalkProcessService dingTalkProcessService = new DingTalkProcessService(dingTalkConfig,
                userService,
                new com.aliyun.dingtalkworkflow_1_0.Client(getConfig()),
                new DingTalkFDTCF());

        //注册到工厂供后续使用
        Factory.registerProcessService(dingTalkProcessService.getChannelName(), dingTalkProcessService);
        return dingTalkProcessService;
    }


    public Config getConfig() {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return config;
    }

}
