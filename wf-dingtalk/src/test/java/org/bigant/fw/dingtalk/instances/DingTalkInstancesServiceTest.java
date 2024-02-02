package org.bigant.fw.dingtalk.instances;

import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.dingtalk.BaseTest;
import org.bigant.wf.form.bean.FormComponent;
import org.bigant.wf.form.component.ComponentDateFormat;
import org.bigant.wf.form.component.bean.AttachmentComponent;
import org.bigant.wf.instances.bean.InstancesStart;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author galen
 * @date 2024/2/117:07
 */
@Slf4j
public class DingTalkInstancesServiceTest extends BaseTest {

    private static final String dingTalkTestProcessCode = System.getenv("dingTalkTestProcessCode");

    @Test
    public void start() {
        log.info("开始测试");
        InstancesStart.InstancesStartBuilder builder = InstancesStart.builder();
        InstancesStart instancesStart = builder.code(dingTalkTestProcessCode)
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
                                new FormComponent()
                                        .setValue("单行输入框", "测试"),
                                new FormComponent()
                                        .setValue("多行输入框", "测试"),
                                new FormComponent()
                                        .setNumberValue("数字输入框", 1),
                                new FormComponent()
                                        .setSelectValue("单选框", "选项1"),
                                new FormComponent()
                                        .setMultiSelectValue("多选框", "选项1", "选项2"),
                                new FormComponent()
                                        .setDateValue("日期", LocalDateTime.now(), ComponentDateFormat.YYYY_MM_DD),
                                new FormComponent()
                                        .setDateRangeValue("开始时间", LocalDateTime.now(), "结束时间", LocalDateTime.now(), ComponentDateFormat.YYYY_MM_DD),
                                new FormComponent()
                                        .setAttachmentValue("附件", Arrays.asList(AttachmentComponent.builder().name("测试.gif").size(100L).url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                                AttachmentComponent.builder().name("测试测试.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                                AttachmentComponent.builder().name("测试测试.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build()))
                        ))
                .build();

        DingTalkInstancesService dingTalkInstancesService = this.getDingTalkInstancesService();
        dingTalkInstancesService.start(instancesStart);

    }

    public DingTalkInstancesService getDingTalkInstancesService() {
        return new DingTalkInstancesService(dingTalkConfig, userService);
    }

}