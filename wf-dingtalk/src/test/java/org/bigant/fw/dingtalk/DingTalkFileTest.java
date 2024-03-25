package org.bigant.fw.dingtalk;

import com.aliyun.dingtalkdrive_1_0.models.AddFileResponseBody;
import com.aliyun.dingtalkstorage_1_0.models.CommitFileResponseBody;
import com.aliyun.dingtalkworkflow_1_0.Client;
import com.aliyun.teaopenapi.models.Config;
import org.bigant.fw.dingtalk.instances.DingTalkInstancesService;
import org.bigant.fw.dingtalk.instances.form.convert.DingTalkAttachmentFDC;
import org.junit.Test;

/**
 * @author galen
 * @date 2024/3/2517:35
 */
public class DingTalkFileTest extends BaseTest {





    public DingTalkFileTest() throws Exception {
    }

    @Test
    public void putFile() throws Exception {
        DingTalkUser talkUser = new DingTalkUser(dingTalkConfig, cache);
        DingTalkAttachmentFDC dingTalkAttachmentFDC = new DingTalkAttachmentFDC(cache, dingTalkConfig, talkUser, new Client(getConfig()));
        String dingtalkUserId = userService.getOtherUserIdByUserId("1", "dingtalk");
        for (int i = 0; i < 10; i++) {
            CommitFileResponseBody commitFileResponseBody =
                    DingTalkFile.uploadFile(talkUser.getUnionId(dingtalkUserId),
                            dingTalkAttachmentFDC.getProcessInstancesSpaces(dingtalkUserId).toString(),
                            "测试文件.xlsx",
                            null,
                            "",
                            dingTalkConfig);

            System.out.println(commitFileResponseBody);
        }


    }

    @Test
    public void putFileOld() throws Exception {
        DingTalkUser talkUser = new DingTalkUser(dingTalkConfig, cache);
        DingTalkAttachmentFDC dingTalkAttachmentFDC = new DingTalkAttachmentFDC(cache, dingTalkConfig, talkUser, new Client(getConfig()));
        String dingtalkUserId = userService.getOtherUserIdByUserId("1", "dingtalk");
        AddFileResponseBody commitFileResponseBody =
                DingTalkFileOld.updateFile(talkUser.getUnionId(dingtalkUserId),
                        dingTalkAttachmentFDC.getProcessInstancesSpaces(dingtalkUserId).toString(),
                        "测试文件.xlsx",
                        System.getenv("filePath"),
                        dingTalkConfig);

        System.out.println(commitFileResponseBody);
    }

    public DingTalkInstancesService getDingTalkInstancesService() {
        try {
            return new DingTalkInstancesService(dingTalkConfig, ccf, userService, new Client(getConfig()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Config getConfig() {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return config;
    }
}