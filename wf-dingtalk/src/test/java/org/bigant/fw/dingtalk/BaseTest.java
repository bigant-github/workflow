package org.bigant.fw.dingtalk;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.teaopenapi.models.Config;
import org.bigant.wf.user.UserService;
import org.bigant.wf.user.vo.User;
import org.junit.Before;

/**
 * 基础测试
 *
 * @author galen
 * @date 2024/1/3111:53
 */
public class BaseTest {
    protected DingTalkConfig dingTalkConfig;
    protected UserService userService;

    @Before
    public void setup() throws Exception {

        String dingTalkAgentId = System.getenv("dingTalkAgentId");
        String dingTalkAppKey = System.getenv("dingTalkAppKey");
        String dingTalkAppSecret = System.getenv("dingTalkAppSecret");
        String dingTalkUserId = System.getenv("dingTalkUserId");
        String dingTalkDeptId = System.getenv("dingTalkDeptId");
        String dingTalkManagerUserId = System.getenv("dingTalkManagerUserId");

        dingTalkConfig = new DingTalkConfig(dingTalkAppKey,
                dingTalkAppSecret,
                dingTalkManagerUserId,
                Long.parseLong(dingTalkAgentId),
                null, null, null, new Client(getConfig()));

        userService = new UserService() {

            @Override
            public User getUser(String id) {
                return null;
            }

            @Override
            public String getUserId(String userId, String type) {
                return null;
            }

            @Override
            public String getDeptId(String deptId, String type) {
                return null;
            }


        };

    }

    public Config getConfig() {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return config;
    }

}
