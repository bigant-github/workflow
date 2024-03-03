package org.bigant.fw.dingtalk.instances.form.convert;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.wf.form.option.DateOption;
import org.bigant.wf.instances.form.ComponentType;
import org.bigant.wf.instances.form.FormData;
import org.bigant.wf.instances.form.FormDataParseAll;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 钉钉日期转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class DingTalkDateFDC extends DingTalkBaseFDC {

    @Override
    public Map<String, String> toOther(FormData data, String dingTalkUserId) {
        return toMap(data.getName(),
                FormDataParseAll.COMPONENT_PARSE_DATE.strToJava(data.getValue()).toDateStr());
    }

    @Override
    public FormData toFormData(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues data) {

        String value = data.getValue();
        DateOption.ComponentDateFormat componentDateFormat = dateType(value);

        return FormData.date(data.getName(),
                this.toLocalDateTime(value, componentDateFormat),
                componentDateFormat);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.MULTI_SELECT;
    }

    @Override
    public Collection<String> getOtherType() {
        return Collections.singletonList("DDDateField");
    }

}
