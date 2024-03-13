package org.bigant.fw.lark;

import com.lark.oapi.Client;
import org.bigant.wf.cache.ICache;
import org.bigant.wf.cache.LocalCache;
import org.bigant.wf.user.UserService;
import org.bigant.wf.user.vo.User;
import org.junit.Before;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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

    protected ICache cache;
    protected String verificationToken = System.getenv("larkEncryptKey");
    protected String encryptKey = System.getenv("larkVerificationToken");


    @Before
    public void before() {


        String larkAppId = System.getenv("larkAppId");
        String larkAppSecret = System.getenv("larkAppSecret");
        String larkUserId = System.getenv("larkUserId");
        String larkDeptId = System.getenv("larkDeptId");

        larkConfig = new LarkConfig(larkAppId, larkAppSecret);
        larkConfig.setClient(Client.newBuilder(larkAppId, larkAppSecret)
                .logReqAtDebug(true) // 在 debug 模式下会打印 http 请求和响应的 headers,body 等信息。
                .build());
        userService = new UserService() {

            @Override
            public User getUser(String id) {
                return User.builder()
                        .userId("1")
                        .userName("galen")
                        .deptNames(Arrays.asList("研发部"))
                        .deptIds(Arrays.asList("1"))
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
                return "2";
            }

            @Override
            public String getDeptIdByOtherDeptId(String otherDeptId, String type) {
                return "3";
            }
        };

        this.larkFile = new LarkFile(larkConfig);
        cache = LocalCache.getInstance();
    }


}
