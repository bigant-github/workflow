package org.bigant.wf.form.bean;

import com.alibaba.fastjson2.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.bigant.wf.form.component.ComponentDateFormat;
import org.bigant.wf.form.component.ComponentType;
import org.bigant.wf.form.component.bean.AmountComponent;
import org.bigant.wf.form.component.bean.AttachmentComponent;
import org.bigant.wf.form.component.bean.DateComponent;
import org.bigant.wf.form.component.bean.DateRangeComponent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@ApiModel("表单字段")
@Data
public class FormComponent {

    @ApiModelProperty("表单字段名称")
    private String name;

    @ApiModelProperty("表单字段值")
    private String value;

    @ApiModelProperty("表单字段类型")
    private ComponentType componentType;


    public static FormComponent text(String name, String value) {
        FormComponent formComponent = new FormComponent();
        return formComponent.setValue(name, value, ComponentType.TEXT);
    }


    public static FormComponent textarea(String name, String value) {
        FormComponent formComponent = new FormComponent();
        return formComponent.setValue(name, value, ComponentType.TEXTAREA);
    }


    public static FormComponent number(String name, Number value) {
        FormComponent formComponent = new FormComponent();
        return formComponent.setValue(name, value, ComponentType.NUMBER);
    }

    public static FormComponent amount(String name, BigDecimal value, AmountComponent.AmountType amountType) {
        FormComponent formComponent = new FormComponent();
        return formComponent.setValue(name, new AmountComponent(value, amountType), ComponentType.AMOUNT);
    }

    public static FormComponent select(String name, String value) {
        FormComponent formComponent = new FormComponent();
        return formComponent.setValue(name, value, ComponentType.SELECT);
    }

    public static FormComponent multiSelect(String name, Collection<String> value) {
        FormComponent formComponent = new FormComponent();
        return formComponent.setValue(name, value, ComponentType.MULTI_SELECT);
    }

    public static FormComponent multiSelect(String name, String... values) {
        FormComponent formComponent = new FormComponent();
        return formComponent.setValue(name, Arrays.asList(values), ComponentType.MULTI_SELECT);
    }

    public static FormComponent attachment(String name, Collection<AttachmentComponent> value) {
        FormComponent formComponent = new FormComponent();
        return formComponent.setValue(name, value, ComponentType.ATTACHMENT);
    }

    public static FormComponent image(String name, List<AttachmentComponent> value) {
        FormComponent formComponent = new FormComponent();
        return formComponent.setValue(name, value, ComponentType.IMAGE);
    }

    public static FormComponent date(String name, LocalDateTime value, ComponentDateFormat dateFormat) {
        FormComponent formComponent = new FormComponent();
        return formComponent.setValue(name, new DateComponent(dateFormat, value), ComponentType.DATE);
    }

    public static FormComponent dateRange(String beginName, String endName, DateRangeComponent value) {
        FormComponent formComponent = new FormComponent();
        return formComponent.setValue(JSONArray.of(beginName, endName).toJSONString(), value, ComponentType.DATE_RANGE);
    }

    public static FormComponent dateRange(String beginName,
                                          LocalDateTime beginDate,
                                          String endName,
                                          LocalDateTime end,
                                          ComponentDateFormat dateFormat) {
        return dateRange(beginName, endName, new DateRangeComponent(dateFormat, beginDate, end));
    }

    public static FormComponent table(String name, Collection<Collection<FormComponent>> table) {
        FormComponent formComponent = new FormComponent();
        return formComponent.setValue(name, table, ComponentType.TABLE);
    }

    private FormComponent setValue(String name, Object value, ComponentType componentType) {
        this.name = name;
        this.componentType = componentType;
        this.value = componentType
                .getParse()
                .toStr(value);

        return this;
    }


}
