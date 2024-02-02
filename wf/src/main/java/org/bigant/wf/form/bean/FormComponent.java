package org.bigant.wf.form.bean;

import com.alibaba.fastjson2.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.bigant.wf.form.component.ComponentDateFormat;
import org.bigant.wf.form.component.ComponentType;
import org.bigant.wf.form.component.bean.AttachmentComponent;
import org.bigant.wf.form.component.bean.DateComponent;
import org.bigant.wf.form.component.bean.DateRangeComponent;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

@ApiModel("表单字段")
@Data
public class FormComponent {

    @ApiModelProperty("表单字段名称")
    private String name;

    @ApiModelProperty("表单字段值")
    private String value;

    @ApiModelProperty("表单字段类型")
    private ComponentType componentType;


    public FormComponent setValue(String name, String value) {
        this.setValue(name, value, ComponentType.TEXT);
        return this;
    }


    public FormComponent setTextareaValue(String name, String value) {
        this.setValue(name, value, ComponentType.TEXTAREA);
        return this;
    }


    public FormComponent setNumberValue(String name, Number value) {
        this.setValue(name, value, ComponentType.NUMBER);
        return this;
    }


    public FormComponent setSelectValue(String name, String value) {
        this.setValue(name, value, ComponentType.SELECT);
        return this;
    }

    public FormComponent setMultiSelectValue(String name, Collection<String> value) {
        this.setValue(name, value, ComponentType.MULTI_SELECT);
        return this;
    }


    public FormComponent setMultiSelectValue(String name, String... values) {
        this.setValue(name, Arrays.asList(values), ComponentType.MULTI_SELECT);
        return this;
    }

    public FormComponent setAttachmentValue(String name, Collection<AttachmentComponent> value) {
        this.setValue(name, value, ComponentType.ATTACHMENT);
        return this;
    }


    public FormComponent setDateValue(String name, LocalDateTime value, ComponentDateFormat dateFormat) {
        this.setValue(name, new DateComponent(dateFormat, value), ComponentType.DATE);
        return this;
    }

    public FormComponent setDateRangeValue(String beginName, String endName, DateRangeComponent value) {
        this.setValue(JSONArray.of(beginName, endName).toJSONString(), value, ComponentType.DATE_RANGE);
        return this;
    }

    public FormComponent setDateRangeValue(String beginName,
                                           LocalDateTime beginDate,
                                           String endName,
                                           LocalDateTime end,
                                           ComponentDateFormat dateFormat) {

        return this.setDateRangeValue(beginName, endName, new DateRangeComponent(dateFormat, beginDate, end));
    }


    private void setValue(String name, Object value, ComponentType componentType) {
        this.name = name;
        this.componentType = componentType;
        this.value = componentType
                .getParse()
                .toStr(value);
    }


}
