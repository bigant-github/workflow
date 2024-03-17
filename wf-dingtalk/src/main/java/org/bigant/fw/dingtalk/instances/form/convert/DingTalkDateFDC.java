package org.bigant.fw.dingtalk.instances.form.convert;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.fw.dingtalk.DingTalkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.process.form.option.DateOption;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.instances.form.FormDataParseAll;

import java.util.Collection;
import java.util.Map;

/**
 * 钉钉日期转换器
 *
 * @author galen
 * date 2024/3/115:29
 */
public class DingTalkDateFDC extends DingTalkBaseFDC {

    @Override
    public Map<String, String> toOther(FormDataItem data, String dingTalkUserId) {
        return toMap(data.getName(),
                FormDataParseAll.COMPONENT_PARSE_DATE.strToJava(data.getValue()).toDateStr());
    }

    @Override
    public FormDataItem toFormData(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues data) {

        String value = data.getValue();
        DateOption.ComponentDateFormat componentDateFormat = dateType(value);

        return FormDataItem.date(data.getName(),
                this.toLocalDateTime(value, componentDateFormat),
                componentDateFormat);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.DATE;
    }

    @Override
    public Collection<String> getOtherType() {
        return DingTalkFormType.DATE.getDingTalkType();
    }

}
