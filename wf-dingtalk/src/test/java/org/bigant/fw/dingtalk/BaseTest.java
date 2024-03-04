package org.bigant.fw.dingtalk;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.teaopenapi.models.Config;
import org.bigant.fw.dingtalk.instances.form.DingTalkFDCF;
import org.bigant.wf.cache.ICache;
import org.bigant.wf.cache.LocalCache;
import org.bigant.wf.user.UserService;
import org.bigant.wf.user.vo.User;
import org.junit.Before;

import java.util.Arrays;
import java.util.Collections;

/**
 * 基础测试
 *
 * @author galen
 * @date 2024/1/3111:53
 */
public class BaseTest {
    protected DingTalkConfig dingTalkConfig;
    protected UserService userService;
    protected DingTalkUser dingTalkUser;
    protected ICache cache = LocalCache.getInstance();
    protected DingTalkFDCF ccf;


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
                null, null, cache, new Client(getConfig()));

        userService = new UserService() {

            @Override
            public User getUser(String id) {
                return User.builder()
                        .userName("galen")
                        .userId("1")
                        .deptName(Collections.singletonList("研发部"))
                        .build();
            }

            @Override
            public String getOtherUserIdByUserId(String userId, String type) {
                return dingTalkUserId;
            }

            @Override
            public String getOtherDeptIdByDeptId(String deptId, String type) {
                return dingTalkDeptId;
            }

            @Override
            public String getUserIdByOtherUserId(String otherUserId, String type) {
                return "tt";
            }

            @Override
            public String getDeptIdByOtherDeptId(String otherDeptId, String type) {
                return "tt";
            }


        };

        dingTalkUser = new DingTalkUser(dingTalkConfig, cache);

        ccf = new DingTalkFDCF(dingTalkConfig, dingTalkUser, cache, new com.aliyun.dingtalkworkflow_1_0.Client(getConfig()));

    }

    public Config getConfig() {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return config;
    }

}
