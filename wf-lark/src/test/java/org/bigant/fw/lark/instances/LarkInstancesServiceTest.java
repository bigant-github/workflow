package org.bigant.fw.lark.instances;


import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.lark.BaseTest;
import org.bigant.fw.lark.process.LarkProcessService;
import org.bigant.wf.instances.form.FormData;
import org.bigant.wf.form.option.AmountOption;
import org.bigant.wf.instances.form.databean.FormDataAttachment;
import org.bigant.wf.instances.form.databean.FormDataImage;
import org.bigant.wf.form.option.DateOption;
import org.bigant.wf.instances.bean.InstanceDetailResult;
import org.bigant.wf.instances.bean.InstanceStart;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author galen
 * @date 2024/2/2217:01
 */
@Slf4j
public class LarkInstancesServiceTest extends BaseTest {



    @Test
    public void start() {
        log.info("开始测试");

        String larkTestProcessCode = System.getenv("larkTestProcessCode");

        InstanceStart.InstanceStartBuilder builder = InstanceStart.builder();
        InstanceStart instanceStart = builder.processCode(larkTestProcessCode)
                .userId("123456789")
                .deptId("123456789")
                .targetSelectUsersAuthMatch(
                        Arrays.asList(InstanceStart.TargetSelectUserAuthMatch.builder()
                                        .userIds(Arrays.asList("1"))
                                        .build()
                                , InstanceStart.TargetSelectUserAuthMatch.builder()
                                        .userIds(Arrays.asList("1"))
                                        .build()
                        ))
                .formData(
                        Arrays.asList(
                                FormData.text("单行文本", "测试"),
                                FormData.textarea("多行文本", "测试"),
                                FormData.number("数字", 1),
                                FormData.amount("金额", new BigDecimal("1.22"), AmountOption.AmountType.CNY),
                                FormData.select("单选", "选项 1"),
                                FormData.multiSelect("多选", "选项 1", "选项 2"),
                                FormData.date("日期", LocalDateTime.now(), DateOption.ComponentDateFormat.YYYY_MM_DD),
                                FormData.dateRange("开始时间", LocalDateTime.now(), "结束时间", LocalDateTime.now(), DateOption.ComponentDateFormat.YYYY_MM_DD_HH_MM),
                                FormData.image("图片", Arrays.asList(
                                        FormDataImage.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                        FormDataImage.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                        FormDataImage.builder().name("测试3.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build())),
                                FormData.attachment("附件", Arrays.asList(
                                        FormDataAttachment.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                        FormDataAttachment.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                        FormDataAttachment.builder().name("测试3.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build())),
                                FormData.table("表格",
                                        Arrays.asList(
                                                Arrays.asList(
                                                        FormData.text("单行文本", "表格单行输入框1"),
                                                        FormData.date("日期", LocalDateTime.now(), DateOption.ComponentDateFormat.YYYY_MM_DD_HH_MM),
                                                        FormData.attachment("附件", Arrays.asList(
                                                                FormDataAttachment.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                                                FormDataAttachment.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build()))
                                                ),
                                                Arrays.asList(
                                                        FormData.text("单行文本", "表格单行输入框2"),
                                                        FormData.date("日期", LocalDateTime.now(), DateOption.ComponentDateFormat.YYYY_MM_DD_HH_MM),
                                                        FormData.attachment("附件", Arrays.asList(
                                                                FormDataAttachment.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                                                FormDataAttachment.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build()))
                                                )))))
                .build();

        LarkInstancesService larkInstancesService = this.getLarkInstancesService();
        larkInstancesService.start(instanceStart);
    }


    public void detail(){
        String larkTestProcessCode = System.getenv("larkTestInstanceCode");

        InstanceDetailResult detail = getLarkInstancesService().detail(larkTestProcessCode);

    }

    public LarkInstancesService getLarkInstancesService() {
        LarkProcessService processService = new LarkProcessService(larkConfig);

        return new LarkInstancesService(larkConfig,
                processService,
                userService);

    }

}