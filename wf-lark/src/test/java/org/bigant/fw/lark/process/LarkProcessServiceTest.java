package org.bigant.fw.lark.process;

import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.lark.BaseTest;
import org.bigant.wf.process.bean.ProcessDetail;
import org.junit.Test;

/**
 * @author galen
 * @date 2024/2/2011:27
 */
@Slf4j
public class LarkProcessServiceTest extends BaseTest {


    @Test
    public void detail() {
        String larkTestProcessCode = System.getenv("larkTestProcessCode");
        ProcessDetail detail = getService().detail(larkTestProcessCode);
        log.info(detail.toString());
    }


    public LarkProcessService getService() {
        return new LarkProcessService(larkConfig);
    }

}