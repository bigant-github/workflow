package spring.test;

import org.bigant.fw.lark.LarkConfig;
import org.bigant.fw.lark.instances.LarkInstancesService;
import org.bigant.fw.lark.process.LarkProcessService;
import org.bigant.wf.user.UserService;
import org.bigant.wf.user.vo.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author galen
 * @date 2024/2/2609:49
 */
@SpringBootApplication
@Configuration
public class ApplicationTestStartSuccess {


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApplicationTestStartSuccess.class, args);
        LarkConfig config = context.getBean(LarkConfig.class);
        assert config == null;
        LarkInstancesService larkInstancesService = context.getBean(LarkInstancesService.class);
        assert larkInstancesService == null;
        LarkProcessService larkProcessService = context.getBean(LarkProcessService.class);
        assert larkProcessService == null;

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
