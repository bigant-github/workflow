package org.bigant.fw.lark;

import org.bigant.wf.user.UserService;
import org.bigant.wf.user.vo.User;
import org.junit.Before;

import java.util.Arrays;

/**
 * 基础测试
 *
 * @author galen
 * @date 2024/2/2011:28
 */
public class BaseTest {

    protected LarkConfig larkConfig;
    protected UserService userService;
    protected LarkFile larkFile;


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
                return User.builder()
                        .userId("1")
                        .userName("galen")
                        .deptName(Arrays.asList("研发部"))
                        .build();

            }

            @Override
            public String getOtherUserIdByUserId(String id, String thirdPartyType) {
                return larkUserId;
            }

            @Override
            public String getOtherDeptIdByDeptId(String deptId, String thirdPartyType) {
                return larkDeptId;
            }

            @Override
            public String getUserIdByOtherUserId(String otherUserId, String type) {
                return null;
            }

            @Override
            public String getByDeptIdByOtherDeptId(String otherDeptId, String type) {
                return null;
            }
        };

        this.larkFile = new LarkFile(larkConfig);
    }


}
