package org.bigant.wf.instances.form;

import com.alibaba.fastjson2.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.bigant.wf.form.option.AmountOption;
import org.bigant.wf.form.option.DateOption;
import org.bigant.wf.instances.form.databean.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@ApiModel("表单字段")
@Data
public class FormData {

    @ApiModelProperty("表单字段名称")
    private String name;

    @ApiModelProperty("表单字段值")
    private String value;

    @ApiModelProperty("表单字段类型")
    private ComponentType componentType;


    public static FormData text(String name, String value) {
        FormData formData = new FormData();
        return formData.setValue(name, value, ComponentType.TEXT);
    }


    public static FormData textarea(String name, String value) {
        FormData formData = new FormData();
        return formData.setValue(name, value, ComponentType.TEXTAREA);
    }


    public static FormData number(String name, Number value) {
        FormData formData = new FormData();
        return formData.setValue(name, value, ComponentType.NUMBER);
    }

    public static FormData amount(String name, BigDecimal value, AmountOption.AmountType amountType) {
        FormData formData = new FormData();
        return formData.setValue(name, new AmountComponent(value, amountType), ComponentType.AMOUNT);
    }

    public static FormData select(String name, String value) {
        FormData formData = new FormData();
        return formData.setValue(name, value, ComponentType.SELECT);
    }

    public static FormData multiSelect(String name, Collection<String> value) {
        FormData formData = new FormData();
        return formData.setValue(name, value, ComponentType.MULTI_SELECT);
    }

    public static FormData multiSelect(String name, String... values) {
        FormData formData = new FormData();
        return formData.setValue(name, Arrays.asList(values), ComponentType.MULTI_SELECT);
    }

    public static FormData attachment(String name, Collection<AttachmentComponent> value) {
        FormData formData = new FormData();
        return formData.setValue(name, value, ComponentType.ATTACHMENT);
    }

    public static FormData image(String name, List<ImageComponent> value) {
        FormData formData = new FormData();
        return formData.setValue(name, value, ComponentType.IMAGE);
    }

    public static FormData date(String name, LocalDateTime value, DateOption.ComponentDateFormat dateFormat) {
        FormData formData = new FormData();
        return formData.setValue(name, new DateComponent(dateFormat, value), ComponentType.DATE);
    }

    public static FormData dateRange(String beginName, String endName, DateRangeComponent value) {
        FormData formData = new FormData();
        return formData.setValue(JSONArray.of(beginName, endName).toJSONString(), value, ComponentType.DATE_RANGE);
    }

    public static FormData dateRange(String beginName,
                                     LocalDateTime beginDate,
                                     String endName,
                                     LocalDateTime end,
                                     DateOption.ComponentDateFormat dateFormat) {
        return dateRange(beginName, endName, new DateRangeComponent(dateFormat, beginDate, end));
    }

    public static FormData table(String name, Collection<Collection<FormData>> table) {
        FormData formData = new FormData();
        return formData.setValue(name, table, ComponentType.TABLE);
    }

    private FormData setValue(String name, Object value, ComponentType componentType) {
        this.name = name;
        this.componentType = componentType;
        this.value = componentType
                .getParse()
                .toStr(value);

        return this;
    }


}
