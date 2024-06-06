package org.bigant.fw.lark.instances;


import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.lark.BaseTest;
import org.bigant.fw.lark.LarkConstant;
import org.bigant.fw.lark.instances.form.LarkFDCF;
import org.bigant.fw.lark.process.LarkProcessService;
import org.bigant.wf.instances.bean.InstanceCancel;
import org.bigant.wf.instances.bean.InstanceDetailResult;
import org.bigant.wf.instances.bean.InstanceStart;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.instances.form.databean.FormDataAttachment;
import org.bigant.wf.instances.form.databean.FormDataImage;
import org.bigant.wf.process.form.option.AmountOption;
import org.bigant.wf.process.form.option.DateOption;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author galen
 * date 2024/2/2217:01
 */
@Slf4j
public class LarkInstancesServiceTest extends BaseTest {


    String instanceCode;

    @Test
    public void start() {
        log.info("开始测试");

        String larkTestProcessCode = System.getProperty("larkTestProcessCode");

        InstanceStart instanceStart = InstanceStart.builder().processCode(larkTestProcessCode)
                .userId("123456789")
                .deptId("123456789")
                .authMatchSelectApproverUsers(
                        Arrays.asList(InstanceStart.AuthMatchNodeUser.builder()
                                        .userIds(Arrays.asList("1"))
                                        .build()
                                , InstanceStart.AuthMatchNodeUser.builder()
                                        .userIds(Arrays.asList("1"))
                                        .build()))
                .formData(
                        Arrays.asList(
                                FormDataItem.text("单行文本", "测试"),
                                FormDataItem.textarea("多行文本", "测试"),
                                FormDataItem.number("数字", 1),
                                FormDataItem.amount("金额", new BigDecimal("1.22"), AmountOption.AmountType.CNY),
                                FormDataItem.select("单选", "选项 1"),
                                FormDataItem.multiSelect("多选", "选项 1", "选项 2"),
                                FormDataItem.date("日期", LocalDateTime.now(), DateOption.ComponentDateFormat.YYYY_MM_DD),
                                FormDataItem.dateRange("开始时间1", LocalDateTime.now(), "结束时间1", LocalDateTime.now(), DateOption.ComponentDateFormat.YYYY_MM_DD_HH_MM),
                                FormDataItem.image("图片", Arrays.asList(
                                        FormDataImage.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                        FormDataImage.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                        FormDataImage.builder().name("测试3.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build())),
                                FormDataItem.attachment("附件", Arrays.asList(
                                        FormDataAttachment.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                        FormDataAttachment.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                        FormDataAttachment.builder().name("测试3.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build())),
                                FormDataItem.table("表格",
                                        Arrays.asList(
                                                Arrays.asList(
                                                        FormDataItem.text("单行文本", "表格单行输入框1"),
                                                        FormDataItem.date("日期", LocalDateTime.now(), DateOption.ComponentDateFormat.YYYY_MM_DD_HH_MM),
                                                        FormDataItem.attachment("附件", Arrays.asList(
                                                                FormDataAttachment.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                                                FormDataAttachment.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build()))
                                                ),
                                                Arrays.asList(
                                                        FormDataItem.text("单行文本", "表格单行输入框2"),
                                                        FormDataItem.date("日期", LocalDateTime.now(), DateOption.ComponentDateFormat.YYYY_MM_DD_HH_MM),
                                                        FormDataItem.attachment("附件", Arrays.asList(
                                                                FormDataAttachment.builder().name("测试1.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build(),
                                                                FormDataAttachment.builder().name("测试2.gif").url("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF").build()))
                                                )))))
                .build();

        LarkInstancesService larkInstancesService = this.getLarkInstancesService();
        this.instanceCode = larkInstancesService.start(instanceStart).getInstanceCode();
    }


    @Test
    public void detail() {
        String larkTestProcessCode = System.getProperty("larkTestInstanceCode");
        InstanceDetailResult detail = getLarkInstancesService().detail("4D312F4B-6052-46DB-A53E-2D011EB669FA");
        log.info(JSONObject.toJSONString(detail));
    }

    @Test
    public void cancel() throws InterruptedException {
        String larkTestProcessCode = System.getProperty("larkTestProcessCode");
        this.start();
        Thread.sleep(20000);
        getLarkInstancesService().cancel(InstanceCancel.builder()
                .processCode(larkTestProcessCode)
                .instanceCode(instanceCode)
                .userId(userService.getOtherUserIdByUserId("1", LarkConstant.NAME))
                .build());
    }


    public LarkInstancesService getLarkInstancesService() {
        LarkProcessService processService = new LarkProcessService(larkConfig);


        return new LarkInstancesService(larkConfig,
                processService,
                userService,
                new LarkFDCF(larkFile), cache, true);

    }

}