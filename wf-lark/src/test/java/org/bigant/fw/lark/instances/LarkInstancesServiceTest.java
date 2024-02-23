package org.bigant.fw.lark.instances;


import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.lark.BaseTest;
import org.bigant.fw.lark.process.LarkProcessService;
import org.bigant.wf.form.bean.FormComponent;
import org.bigant.wf.form.component.ComponentDateFormat;
import org.bigant.wf.form.component.bean.AttachmentComponent;
import org.bigant.wf.instances.bean.InstancesStart;
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

        InstancesStart.InstancesStartBuilder builder = InstancesStart.builder();
        InstancesStart instancesStart = builder.code(larkTestProcessCode)
                .userId("123456789")
                .deptId("123456789")
                .targetSelectUsersAuthMatch(
                        Arrays.asList(InstancesStart.TargetSelectUserAuthMatch.builder()
                                        .userIds(Arrays.asList("1"))
                                        .build()
                                , InstancesStart.TargetSelectUserAuthMatch.builder()
                                        .userIds(Arrays.asList("1"))
                                        .build()
                        ))
                .formComponents(
                        Arrays.asList(
                                FormComponent.text("单行文本", "测试"),
                                FormComponent.textarea("多行文本", "测试"),
                                FormComponent.number("数字", 1),
                                FormComponent.amount("金额", new BigDecimal("1.22")),
                                FormComponent.select("单选", "选项 1"),
                                FormComponent.multiSelect("多选", "选项 1", "选项 2"),
                                FormComponent.date("日期", LocalDateTime.now(), ComponentDateFormat.YYYY_MM_DD),
                                FormComponent.dateRange("开始时间", LocalDateTime.now(), "结束时间", LocalDateTime.now(), ComponentDateFormat.YYYY_MM_DD_HH_MM),
                                FormComponent.image("图片", Arrays.asList(
                                        AttachmentComponent.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                        AttachmentComponent.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                        AttachmentComponent.builder().name("测试3.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build())),
                                FormComponent.attachment("附件", Arrays.asList(
                                        AttachmentComponent.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                        AttachmentComponent.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                        AttachmentComponent.builder().name("测试3.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build())),
                                FormComponent.table("表格",
                                        Arrays.asList(
                                                Arrays.asList(
                                                        FormComponent.text("单行文本", "表格单行输入框1"),
                                                        FormComponent.date("日期", LocalDateTime.now(), ComponentDateFormat.YYYY_MM_DD_HH_MM),
                                                        FormComponent.attachment("附件", Arrays.asList(
                                                                AttachmentComponent.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                                                AttachmentComponent.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build()))
                                                ),
                                                Arrays.asList(
                                                        FormComponent.text("单行文本", "表格单行输入框2"),
                                                        FormComponent.date("日期", LocalDateTime.now(), ComponentDateFormat.YYYY_MM_DD_HH_MM),
                                                        FormComponent.attachment("附件", Arrays.asList(
                                                                AttachmentComponent.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                                                AttachmentComponent.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build()))
                                                )))))
                .build();

        LarkInstancesService larkInstancesService = this.getLarkInstancesService();
        larkInstancesService.start(instancesStart);
    }

    public LarkInstancesService getLarkInstancesService() {
        LarkProcessService processService = new LarkProcessService(larkConfig);

        return new LarkInstancesService(larkConfig,
                userService,
                processService);

    }

}