package org.bigant.fw.lark;

import org.bigant.wf.user.UserService;
import org.bigant.wf.user.vo.User;
import org.junit.Before;

/**
 * 基础测试
 *
 * @author galen
 * @date 2024/2/2011:28
 */
public class BaseTest {

    protected LarkConfig larkConfig;
    protected UserService userService;


    @Before
    public void before() {


        String larkAppId = System.getenv("larkAppId");
        String larkAppSecret = System.getenv("larkAppSecret");
        String larkUserId = System.getenv("larkUserId");
        String larkDeptId = System.getenv("larkDeptId");

        larkConfig = new LarkConfig(larkAppId, larkAppSecret, "");

        userService = new UserService() {

            @Override
            public User getUser(String id) {
                return null;
            }

            @Override
            public String getOtherUserIdByUserId(String id, String thirdPartyType) {
                return larkUserId;
            }

            @Override
            public String getOtherDeptIdByDeptId(String deptId, String thirdPartyType) {
                return larkDeptId;
            }
        };
    }


}
