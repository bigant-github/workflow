package org.bigant.fw.dingtalk.process;


import com.alibaba.fastjson2.JSONObject;
import com.aliyun.dingtalkworkflow_1_0.Client;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.dingtalk.BaseTest;
import org.bigant.fw.dingtalk.process.form.DingTalkFDTCF;
import org.bigant.wf.process.bean.ProcessDetail;
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

    private static final String dingTalkTestProcessCode = System.getenv("dingTalkTestProcessCode");

    @Test
    public void testAllPage() {

        // Run the test
        List<ProcessPage> processPageStream =
                getProcessService()
                        .allPage(new ProcessPageQuery());

        log.info(processPageStream.toString());
    }


    @Test
    public void detail() {
        log.info("钉钉-测试查询详情开始");
        ProcessDetail detail = getProcessService()
                .detail(dingTalkTestProcessCode);
        log.info("钉钉-测试查询详情成功。data:{}", JSONObject.toJSONString(detail));
    }


    DingTalkProcessService getProcessService() {
        try {
            return new DingTalkProcessService(dingTalkConfig, userService, new Client(getConfig()), new DingTalkFDTCF());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}