package org.bigant.fw.lark.process;

import com.alibaba.fastjson2.TypeReference;
import com.lark.oapi.Client;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.service.approval.v4.model.GetApprovalReq;
import com.lark.oapi.service.approval.v4.model.GetApprovalResp;
import com.lark.oapi.service.approval.v4.model.GetApprovalRespBody;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.lark.LarkConfig;
import org.bigant.fw.lark.LarkConstant;
import org.bigant.wf.exception.WfException;
import org.bigant.wf.form.component.ComponentType;
import org.bigant.wf.form.option.MultiSelectOption;
import org.bigant.wf.form.option.SelectOption;
import org.bigant.wf.process.IProcessService;
import org.bigant.wf.process.bean.ProcessDetail;
import org.bigant.wf.process.bean.ProcessPage;
import org.bigant.wf.process.bean.ProcessPageQuery;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 飞书审批流service
 *
 * @author galen
 * @date 2024/2/1909:24
 */
@Slf4j
@AllArgsConstructor
public class LarkProcessService implements IProcessService {

    LarkConfig config;

    public final static Type LIST_MAP = new TypeReference<List<Map<String, Object>>>() {
    }.getType();

    public final static Type MAP = new TypeReference<Map<String, Object>>() {
    }.getType();

    @Override
    public List<ProcessPage> page(ProcessPageQuery query, String userId) {
        return null;
    }

    @Override
    public List<ProcessPage> allPage(ProcessPageQuery query) {
        return null;
    }


    @Override
    public ProcessDetail detail(String code) {

        log.debug("lark-query process detail, code:{}", code);

        // 构建client
        Client client = config.getClient();

        try {
            GetApprovalResp resp = client.approval()
                    .approval()
                    .get(GetApprovalReq.newBuilder()
                            .approvalCode(code)
                            .build());


            // 处理服务端错误
            if (!resp.success()) {
                String errMsg = String.format("lark-query process detail error,code:%s,msg:%s,reqId:%s", resp.getCode(), resp.getMsg(), resp.getRequestId());
                log.error(errMsg);
                throw new WfException(errMsg);
            }

            log.debug("lark-query process detail success,code:{},rsp:{}", code, Jsons.DEFAULT.toJson(resp.getData()));

            GetApprovalRespBody data = resp.getData();
            List<Map<String, Object>> formItems = Jsons.DEFAULT.fromJson(data.getForm(), LIST_MAP);

            List<ProcessDetail.FormItem> formItemList = formItems.stream()
                    .map(this::convertFormItem)
                    .collect(Collectors.toList());

            return ProcessDetail.builder()
                    .code(code)
                    .name(data.getApprovalName())
                    .iconUrl(null)
                    .form(formItemList)
                    .build();

        } catch (Exception e) {
            String errMsg = String.format("lark-query process detail error,code:%s", code);
            log.error(errMsg, e);
            throw new WfException(errMsg, e);
        }

    }


    /**
     * 将飞书组件转换为wf组件
     * 单行文本	input
     * 多行文本	textarea
     * 数字	number
     * 金额	amount
     * 日期	date
     * 日期区间	dateInterval
     * 计算公式	formula
     * 附件	attachment/attachmentV2
     * 图片	image/imageV2
     * 联系人	contact
     * 关联审批	connect
     * 地址	address
     * 电话	telephone
     * 请假控件组	leaveGroup
     * 加班控件组	workGroup
     * 换班控件组	shiftGroup
     * 补卡控件组	remedyGroup
     * 出差控件组	tripGroup
     * 外出控件组	outGroup
     * 单选	radio/radioV2
     * 多选	checkbox/checkboxV2
     * 表格 fieldList
     *
     * @param item
     * @return
     */
    public ProcessDetail.FormItem convertFormItem(Map<String, Object> item) {

        switch (item.get("type").toString()) {
            case "input":
                return ProcessDetail.FormItem.builder()
                        .name(item.get("name").toString())
                        .id(item.get("id").toString())
                        .type(ComponentType.TEXT)
                        .build();
            case "textarea":
                return ProcessDetail.FormItem.builder()
                        .name(item.get("name").toString())
                        .id(item.get("id").toString())
                        .type(ComponentType.TEXTAREA)
                        .build();
            case "number":
                return ProcessDetail.FormItem.builder()
                        .name(item.get("name").toString())
                        .id(item.get("id").toString())
                        .type(ComponentType.NUMBER)
                        .build();
            case "amount":
                return ProcessDetail.FormItem.builder()
                        .name(item.get("name").toString())
                        .id(item.get("id").toString())
                        .type(ComponentType.AMOUNT)
                        .build();
            case "date":
                return ProcessDetail.FormItem.builder()
                        .name(item.get("name").toString())
                        .id(item.get("id").toString())
                        .type(ComponentType.DATE)
                        .build();
            case "dateInterval":
                Map<String, Object> option = (Map<String, Object>) item.get("option");
                List<String> names = Arrays.asList(option.get("start").toString(), option.get("end").toString());
                return ProcessDetail.FormItem.builder()
                        .name(Jsons.DEFAULT.toJson(names))
                        .id(item.get("id").toString())
                        .type(ComponentType.DATE_RANGE)
                        .build();
            case "attachment":
            case "attachmentV2":
                return ProcessDetail.FormItem.builder()
                        .name(item.get("name").toString())
                        .id(item.get("id").toString())
                        .type(ComponentType.ATTACHMENT)
                        .build();
            case "image":
            case "imageV2":
                return ProcessDetail.FormItem.builder()
                        .name(item.get("name").toString())
                        .id(item.get("id").toString())
                        .type(ComponentType.IMAGE)
                        .build();
            case "radio":
            case "radioV2":
                List<Map<String, String>> radioOption = (List<Map<String, String>>) item.get("option");
                List<SelectOption.Option> radioOptions = radioOption.stream()
                        .map(x -> SelectOption.Option.builder().name(x.get("text")).value(x.get("value")).build())
                        .collect(Collectors.toList());
                return ProcessDetail.FormItem.builder()
                        .name(item.get("name").toString())
                        .id(item.get("id").toString())
                        .option(SelectOption.builder().options(radioOptions).build())
                        .type(ComponentType.SELECT)
                        .build();
            case "checkbox":
            case "checkboxV2":
                List<Map<String, String>> checkboxOption = (List<Map<String, String>>) item.get("option");
                List<MultiSelectOption.Option> checkboxOptions = checkboxOption.stream()
                        .map(x -> MultiSelectOption.Option.builder().name(x.get("text")).value(x.get("value")).build())
                        .collect(Collectors.toList());
                return ProcessDetail.FormItem.builder()
                        .name(item.get("name").toString())
                        .id(item.get("id").toString())
                        .option(MultiSelectOption.builder().options(checkboxOptions).build())
                        .type(ComponentType.MULTI_SELECT)
                        .build();
            case "fieldList":
                List<Map<String, Object>> children = (List<Map<String, Object>>) item.get("children");
                List<ProcessDetail.FormItem> childrenItems =
                        children.stream().map(this::convertFormItem).collect(Collectors.toList());
                return ProcessDetail.FormItem.builder()
                        .name(item.get("name").toString())
                        .id(item.get("id").toString())
                        .type(ComponentType.TABLE)
                        .children(childrenItems)
                        .build();
            default:
                return ProcessDetail.FormItem.builder()
                        .name(item.get("name").toString())
                        .id(item.get("id").toString())
                        .type(ComponentType.UNKNOWN)
                        .build();
        }
    }

    @Override
    public String getType() {
        return LarkConstant.NAME;
    }
}
