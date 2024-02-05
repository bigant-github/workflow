package org.bigant.fw.dingtalk;

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
    public void setup() {

        String dingTalkAgentId = System.getenv("dingTalkAgentId");
        String dingTalkAppKey = System.getenv("dingTalkAppKey");
        String dingTalkAppSecret = System.getenv("dingTalkAppSecret");
        String dingTalkManagerUserId = System.getenv("dingTalkManagerUserId");
        String dingTalkManagerDeptId = System.getenv("dingTalkManagerDeptId");


        dingTalkConfig = new DingTalkConfig(dingTalkAppKey,
                dingTalkAppSecret,
                dingTalkManagerUserId,
                Long.parseLong(dingTalkAgentId));

        userService = new UserService() {

            @Override
            public User getUser(String id) {
                return null;
            }

            @Override
            public String getThirdPartyId(String id, String thirdPartyType) {
                return dingTalkManagerUserId;
            }

            @Override
            public String getThirdDeptId(String deptId, String thirdPartyType) {
                return dingTalkManagerDeptId;
            }
        };

    }


}
