package org.bigant.fw.lark.instances.form.convert;

import com.alibaba.fastjson2.JSONObject;
import org.bigant.fw.lark.LarkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.process.form.option.DateOption;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.instances.form.FormDataParseAll;
import org.bigant.wf.instances.form.databean.FormDataDate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;

/**
 * 钉钉日期转换器
 *
 * @author galen
 * date 2024/3/115:29
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
    public FormDataItem toFormData(LarkBaseFDC.ToOtherParam data) {
        JSONObject formObj = data.getFormObj();

        org.bigant.wf.process.form.FormDetailItem detailItem =
                data.getFormDetailItemMap().get(formObj.getString("id"));

        return FormDataItem.date(data.getFormObj().getString("name"),
                LocalDateTime.parse(data.getFormObj().getString("value"),
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                , DateOption.ComponentDateFormat.YYYY_MM_DD_HH_MM);

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
