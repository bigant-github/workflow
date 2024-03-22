package org.bigant.fw.dingtalk.instances;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.dingtalkworkflow_1_0.Client;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.dingtalk.BaseTest;
import org.bigant.wf.instances.bean.InstanceDetailResult;
import org.bigant.wf.instances.bean.InstancePreview;
import org.bigant.wf.instances.bean.InstancePreviewResult;
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
import java.util.List;

/**
 * @author galen
 * date 2024/2/117:07
 */
@Slf4j
public class DingTalkInstancesServiceTest extends BaseTest {

    private static final String dingTalkTestProcessCode = System.getenv("dingTalkTestProcessCode");
    private static final String dingTalkTestInstanceCode = System.getenv("dingTalkTestInstanceCode");

    @Test
    public void start() {
        log.info("发起审批实例开始测试。");
        InstanceStart.InstanceStartBuilder builder = InstanceStart.builder();
        InstanceStart instanceStart = builder
                .processCode(dingTalkTestProcessCode)
                .userId("123456789")
                .deptId("123456789")
                .authMatchSelectApproverUsers(
                        Arrays.asList(InstanceStart.AuthMatchNodeUser.builder()
                                        .userIds(Arrays.asList("1"))
                                        .build()
                                , InstanceStart.AuthMatchNodeUser.builder()
                                        .userIds(Arrays.asList("1"))
                                        .build()
                        ))
/*                .autoMathSelectCcUsers(
                        Arrays.asList(InstanceStart.AuthMatchNodeUser.builder()
                                .userIds(Arrays.asList("1"))
                                .build()
                        )
                )*/
                .formData(
                        Arrays.asList(
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
                                                ))),
                                FormDataItem.joinInstance("关联审批",
                                        Arrays.asList(dingTalkTestInstanceCode))))
                .build();

        DingTalkInstancesService dingTalkInstancesService = this.getDingTalkInstancesService();
        dingTalkInstancesService.start(instanceStart);

    }


    @Test
    public void detail() {
        String instanceCode = System.getenv("dingTalkTestInstanceCode");
        log.info("查询审批实例开始测试。instanceCode:{}", instanceCode);
        InstanceDetailResult detail = getDingTalkInstancesService().detail(instanceCode);
        log.info("查询审批实例开始测试通过。instanceCode:{},data:{}", instanceCode, JSONObject.toJSONString(detail));

    }


    @Test
    public void preview() {
        log.info("预览审批开始测试。");
        log.info("预览审批开始测试通过。");

        List<FormDataItem> formData = JSONArray.parseArray("[\n" +
                "    {\n" +
                "        \"name\": \"类别\",\n" +
                "        \"value\": \"费用报销\",\n" +
                "        \"componentType\": \"SELECT\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\": \"收款方开户名\",\n" +
                "        \"value\": \"石家庄平衡裕华门诊部\",\n" +
                "        \"componentType\": \"TEXT\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\": \"收款方开户行\",\n" +
                "        \"value\": \"中国光大银行中国光大银行股份有限公司石家庄体育大街支行\",\n" +
                "        \"componentType\": \"TEXT\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\": \"收款方银行账号\",\n" +
                "        \"value\": \"79400188000000991\",\n" +
                "        \"componentType\": \"TEXT\"\n" +
                "    }" +
/*                ",\n" +
                "    {\n" +
                "        \"name\": \"预算\",\n" +
                "        \"value\": \"内\",\n" +
                "        \"componentType\": \"SELECT\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\": \"单据\",\n" +
                "        \"value\": \"1\",\n" +
                "        \"componentType\": \"NUMBER\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\": \"产品线\",\n" +
                "        \"value\": \"岗位外包\",\n" +
                "        \"componentType\": \"TEXT\"\n" +
                "    },\n" +*/
                "    {\n" +
                "        \"name\": \"付款公司全称\",\n" +
                "        \"value\": \"河北搜才商务服务有限公司\",\n" +
                "        \"componentType\": \"SELECT\"\n" +
                "    }" +
/*                ",\n" +
                "    {\n" +
                "        \"name\": \"领导审批\",\n" +
                "        \"value\": \"满艺|发起|2024-03-21;\\n满艺|同意|2024-03-21;\\n\",\n" +
                "        \"componentType\": \"TEXTAREA\"\n" +
                "    }" +*/
                ",\n" +
                "    {\n" +
                "        \"name\": \"费用报销明细\",\n" +
                "        \"value\": \"[[{\\\"componentType\\\":\\\"SELECT\\\",\\\"name\\\":\\\"报销项目\\\",\\\"value\\\":\\\"福利费\\\"}" +
                ",{\\\"componentType\\\":\\\"TEXT\\\",\\\"name\\\":\\\"摘要\\\"}" +
                ",{\\\"componentType\\\":\\\"AMOUNT\\\",\\\"name\\\":\\\"报销金额（元）\\\",\\\"value\\\":\\\"{\\\\\\\"amount\\\\\\\":3000000,\\\\\\\"amountType\\\\\\\":\\\\\\\"CNY\\\\\\\"}\\\"}]]\",\n" +
                "        \"componentType\": \"TABLE\"\n" +
                "    }" +
                /*",\n" +
                "    {\n" +
                "        \"name\": \"附件/图片\",\n" +
                "        \"value\": \"[{\\\"name\\\":\\\"深泽体检费3636.jpg\\\",\\\"url\\\":\\\"\\\"}]\",\n" +
                "        \"componentType\": \"ATTACHMENT\"\n" +
                "    }\n" +*/
                "]", FormDataItem.class);
        InstancePreview preview = InstancePreview.builder()
                .processCode("")
                .formData(formData)
                .build();


        InstancePreviewResult result =
                getDingTalkInstancesService()
                        .preview(preview);
    }

    public DingTalkInstancesService getDingTalkInstancesService() {
        try {
            return new DingTalkInstancesService(dingTalkConfig, ccf, userService, new Client(getConfig()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}