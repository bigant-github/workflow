package org.bigant.fw.dingtalk.process;


import com.aliyun.dingtalkworkflow_1_0.Client;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.dingtalk.BaseTest;
import org.bigant.wf.process.bean.ProcessPage;
import org.bigant.wf.process.bean.ProcessPageQuery;
import org.junit.Test;

import java.util.List;

/**
 * @author galen
 * @date 2024/1/3111:48
 */
@Slf4j
public class DingTalkFormServiceTest extends BaseTest {


    @Test
    public void testAllPage() {

        // Run the test
        List<ProcessPage> processPageStream =
                getFormService()
                        .allPage(new ProcessPageQuery());

        log.info(processPageStream.toString());
    }

    DingTalkProcessService getFormService() {
        try {
            return new DingTalkProcessService(dingTalkConfig, userService, new Client(getConfig()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}