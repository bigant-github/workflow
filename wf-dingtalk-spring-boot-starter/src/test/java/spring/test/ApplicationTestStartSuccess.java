package spring.test;

import org.bigant.fw.dingtalk.DingTalkConfig;
import org.bigant.fw.dingtalk.instances.DingTalkInstancesService;
import org.bigant.fw.dingtalk.process.DingTalkProcessService;
import org.bigant.wf.user.UserService;
import org.bigant.wf.user.vo.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author galen
 * @date 2024/2/2609:49
 */
@SpringBootApplication
@Configuration
public class ApplicationTestStartSuccess {


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApplicationTestStartSuccess.class, args);
        DingTalkConfig bean = context.getBean(DingTalkConfig.class);
        assert bean == null;
        DingTalkInstancesService dingTalkInstancesService = context.getBean(DingTalkInstancesService.class);
        assert dingTalkInstancesService == null;
        DingTalkProcessService dingTalkProcessService = context.getBean(DingTalkProcessService.class);
        assert dingTalkProcessService == null;

    }


    @Bean
    public UserService userService() {
        return new UserService() {

            @Override
            public User getUser(String id) {
                return null;
            }

            @Override
            public String getThirdPartyId(String userId, String thirdPartyType) {
                return null;
            }

            @Override
            public String getThirdDeptId(String deptId, String thirdPartyType) {
                return null;
            }
        };
    }


}
