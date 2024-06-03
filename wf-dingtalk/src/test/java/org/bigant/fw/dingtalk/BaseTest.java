package org.bigant.fw.dingtalk;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.teaopenapi.models.Config;
import org.bigant.fw.dingtalk.instances.form.DingTalkFDCF;
import org.bigant.wf.cache.ICache;
import org.bigant.wf.cache.LocalCache;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.instances.form.databean.FormDataAttachment;
import org.bigant.wf.instances.form.databean.FormDataImage;
import org.bigant.wf.process.form.option.AmountOption;
import org.bigant.wf.process.form.option.DateOption;
import org.bigant.wf.user.UserService;
import org.bigant.wf.user.vo.User;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * 基础测试
 *
 * @author galen
 * date 2024/1/3111:53
 */
public class BaseTest {
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    protected DingTalkConfig dingTalkConfig;
    protected UserService userService;
    protected DingTalkUser dingTalkUser;
    protected ICache cache = LocalCache.getInstance();
    protected DingTalkFDCF ccf;


    @Before
    public void setup() throws Exception {

        loadEnv();


        String dingTalkAgentId = System.getProperty("dingTalkAgentId");
        String dingTalkAppKey = System.getProperty("dingTalkAppKey");
        String dingTalkAppSecret = System.getProperty("dingTalkAppSecret");
        String dingTalkUserId = System.getProperty("dingTalkUserId");
        String dingTalkDeptId = System.getProperty("dingTalkDeptId");
        String dingTalkManagerUserId = System.getProperty("dingTalkManagerUserId");

        dingTalkConfig = new DingTalkConfig(dingTalkAppKey,
                dingTalkAppSecret,
                dingTalkManagerUserId,
                Long.parseLong(dingTalkAgentId),
                cache,
                new Client(getConfig()));

        userService = new UserService() {

            @Override
            public User getUser(String id) {
                return User.builder()
                        .userName("galen")
                        .userId("1")
                        .deptNames(Collections.singletonList("研发部"))
                        .deptIds(Arrays.asList("1"))
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


    public void loadEnv() {
        String envPath = System.getProperty("user.dir").substring(0, System.getProperty("user.dir").lastIndexOf(File.separator));
        log.info("load env {}", envPath);
        File envFile = new File(envPath + File.separator + ".env");
        if (envFile.exists()) {
            try {
                Properties properties = new Properties();
                properties.load(Files.newInputStream(envFile.toPath()));

                for (String env : properties.stringPropertyNames()) {

                    System.setProperty(env, properties.getProperty(env));
                }
            } catch (Exception e) {
                log.error("load env error", e);
            }
        } else {
            log.warn("env file not found {}", envPath);
        }

    }

    public Config getConfig() {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return config;
    }

    public List<FormDataItem> instanceStartForm() {
        return Arrays.asList(
                FormDataItem.text("单行输入框", "测试"),
                FormDataItem.text("多行输入框", "测试"),
                FormDataItem.number("数字输入框", 1),
                FormDataItem.amount("金额（元）", new BigDecimal("1.23"), AmountOption.AmountType.CNY),
                FormDataItem.select("单选框", "选项1"),
                FormDataItem.multiSelect("多选框", "选项1", "选项2"),
                FormDataItem.date("日期", LocalDateTime.now(), DateOption.ComponentDateFormat.YYYY_MM_DD),
                FormDataItem.dateRange("开始时间", LocalDateTime.now(), "结束时间", LocalDateTime.now(), DateOption.ComponentDateFormat.YYYY_MM_DD),
                FormDataItem.attachment("附件", Arrays.asList(
                        FormDataAttachment.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                        FormDataAttachment.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                        FormDataAttachment.builder().name("测试3.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build())),
                FormDataItem.image("图片", Arrays.asList(
                        FormDataImage.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                        FormDataImage.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                        FormDataImage.builder().name("测试3.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build())),
                FormDataItem.table("表格",
                        Arrays.asList(
                                Arrays.asList(
                                        FormDataItem.text("单行输入框", "表格单行输入框1"),
                                        FormDataItem.date("日期", LocalDateTime.now(), DateOption.ComponentDateFormat.YYYY_MM_DD_HH_MM),
                                        FormDataItem.attachment("附件", Arrays.asList(
                                                FormDataAttachment.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                                FormDataAttachment.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build()))
                                ),
                                Arrays.asList(
                                        FormDataItem.text("单行输入框", "表格单行输入框2"),
                                        FormDataItem.date("日期", LocalDateTime.now(), DateOption.ComponentDateFormat.YYYY_MM_DD_HH_MM),
                                        FormDataItem.attachment("附件", Arrays.asList(
                                                FormDataAttachment.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                                FormDataAttachment.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build()))
                                ))));
    }

}
