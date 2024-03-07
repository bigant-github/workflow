package org.bigant.wf.instances.form;

import com.alibaba.fastjson2.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.bigant.wf.ComponentType;
import org.bigant.wf.process.form.option.AmountOption;
import org.bigant.wf.process.form.option.DateOption;
import org.bigant.wf.instances.form.databean.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

@ApiModel("表单字段")
@Data
public class FormDataItem {

    @ApiModelProperty("表单字段名称")
    private String name;

    @ApiModelProperty("表单字段值")
    private String value;

    @ApiModelProperty("表单字段类型")
    private ComponentType componentType;


    public static FormDataItem text(String name, String value) {
        FormDataItem formDataItem = new FormDataItem();
        return formDataItem.setValue(name
                , FormDataParseAll.COMPONENT_PARSE_TEXT.toStr(value)
                , ComponentType.TEXT);
    }


    public static FormDataItem textarea(String name, String value) {
        FormDataItem formDataItem = new FormDataItem();

        return formDataItem.setValue(name,
                FormDataParseAll.COMPONENT_PARSE_TEXTAREA.toStr(value),
                ComponentType.TEXTAREA);
    }


    public static FormDataItem number(String name, Number value) {
        FormDataItem formDataItem = new FormDataItem();
        return formDataItem.setValue(name,
                FormDataParseAll.COMPONENT_PARSE_NUMBER.toStr(value),
                ComponentType.NUMBER);
    }

    public static FormDataItem amount(String name, BigDecimal value, AmountOption.AmountType amountType) {
        FormDataItem formDataItem = new FormDataItem();
        return formDataItem.setValue(name,
                FormDataParseAll.COMPONENT_PARSE_AMOUNT.toStr(new FormDataAmount(value, amountType)),
                ComponentType.AMOUNT);
    }

    public static FormDataItem select(String name, String value) {
        FormDataItem formDataItem = new FormDataItem();
        return formDataItem.setValue(name,
                FormDataParseAll.COMPONENT_PARSE_SELECT.toStr(value),
                ComponentType.SELECT);
    }

    public static FormDataItem multiSelect(String name, Collection<String> value) {
        FormDataItem formDataItem = new FormDataItem();
        return formDataItem.setValue(name,
                FormDataParseAll.COMPONENT_PARSE_MULTI_SELECT.toStr(value),
                ComponentType.MULTI_SELECT);
    }

    public static FormDataItem multiSelect(String name, String... values) {
        FormDataItem formDataItem = new FormDataItem();
        return formDataItem.setValue(name,
                FormDataParseAll.COMPONENT_PARSE_MULTI_SELECT.toStr(Arrays.asList(values)),
                ComponentType.MULTI_SELECT);
    }

    public static FormDataItem attachment(String name, Collection<FormDataAttachment> value) {
        FormDataItem formDataItem = new FormDataItem();
        return formDataItem.setValue(name,
                FormDataParseAll.COMPONENT_PARSE_ATTACHMENT.toStr(value),
                ComponentType.ATTACHMENT);
    }

    public static FormDataItem image(String name, Collection<FormDataImage> value) {
        FormDataItem formDataItem = new FormDataItem();
        return formDataItem.setValue(name,
                FormDataParseAll.COMPONENT_PARSE_IMAGE.toStr(value),
                ComponentType.IMAGE);
    }

    public static FormDataItem date(String name, LocalDateTime value, DateOption.ComponentDateFormat dateFormat) {
        FormDataItem formDataItem = new FormDataItem();
        return formDataItem.setValue(name,
                FormDataParseAll.COMPONENT_PARSE_DATE.toStr(new FormDataDate(dateFormat, value)),
                ComponentType.DATE);
    }

    public static FormDataItem dateRange(String beginName, String endName, FormDataDateRange value) {
        FormDataItem formDataItem = new FormDataItem();
        return formDataItem.setValue(JSONArray.of(beginName, endName).toJSONString(),
                FormDataParseAll.COMPONENT_PARSE_DATE_RANGE.toStr(value),
                ComponentType.DATE_RANGE);
    }

    public static FormDataItem dateRange(String beginName,
                                         LocalDateTime beginDate,
                                         String endName,
                                         LocalDateTime end,
                                         DateOption.ComponentDateFormat dateFormat) {
        return dateRange(beginName, endName, new FormDataDateRange(dateFormat, beginDate, end));
    }

    public static FormDataItem table(String name, Collection<Collection<FormDataItem>> value) {
        FormDataItem formDataItem = new FormDataItem();
        return formDataItem.setValue(name,
                FormDataParseAll.COMPONENT_PARSE_TABLE.toStr(value),
                ComponentType.TABLE);
    }

    public static FormDataItem joinInstance(String name, String... value) {
        return joinInstance(name, Arrays.asList(value));
    }

    public static FormDataItem joinInstance(String name, Collection<String> value) {
        FormDataItem formDataItem = new FormDataItem();
        return formDataItem.setValue(name,
                FormDataParseAll.COMPONENT_PARSE_JOIN_INSTANCE.toStr(value),
                ComponentType.JOIN_INSTANCE);
    }


    private FormDataItem setValue(String name, String value, ComponentType componentType) {
        this.name = name;
        this.componentType = componentType;
        this.value = value;
        return this;
    }


}
