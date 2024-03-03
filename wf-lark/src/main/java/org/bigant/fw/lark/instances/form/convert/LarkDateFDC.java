package org.bigant.fw.lark.instances.form.convert;

import com.alibaba.fastjson2.JSONObject;
import org.bigant.fw.lark.LarkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.FormData;
import org.bigant.wf.instances.form.FormDataParseAll;
import org.bigant.wf.instances.form.databean.FormDataDate;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;

/**
 * 钉钉日期转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class LarkDateFDC extends LarkBaseFDC {

    @Override
    public Map<String, Object> toOther(LarkBaseFDC.FormItemConvert component) {

        FormDataDate formDataDate = FormDataParseAll
                .COMPONENT_PARSE_DATE
                .strToJava(component.getFormComponents().getValue());

        return this.base(component, "date", formDataDate.getDate().atOffset(ZoneOffset.ofHours(8))
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }

    @Override
    public FormData toFormData(JSONObject data) {

        /*String value = data.getValue();
        DateOption.ComponentDateFormat componentDateFormat = dateType(value);

        return FormData.date(data.getName(),
                this.toLocalDateTime(value, componentDateFormat),
                componentDateFormat);*/

        return null;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.DATE;
    }

    @Override
    public Collection<String> getOtherType() {
        return LarkFormType.DATE.getLarkType();
    }

}
