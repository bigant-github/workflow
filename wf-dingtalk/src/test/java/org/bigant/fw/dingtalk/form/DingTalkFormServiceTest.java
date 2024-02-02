package org.bigant.fw.dingtalk.form;


import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.dingtalk.BaseTest;
import org.bigant.wf.form.bean.FormPage;
import org.bigant.wf.form.bean.FormPageQuery;
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
        List<FormPage> formPageStream =
                getFormService()
                        .allPage(new FormPageQuery());

        log.info(formPageStream.toString());
    }

    DingTalkFormService getFormService() {
        return new DingTalkFormService(dingTalkConfig, userService);
    }
}