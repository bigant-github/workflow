package org.bigant.fw.dingtalk.instances.form.convert;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.wf.instances.form.FormData;
import org.bigant.wf.instances.form.FormDataParseAll;
import org.bigant.wf.instances.form.ComponentType;

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
    public Map<String, String> toOther(FormData component, String dingTalkUserId) {
        return toMap(component.getName(),
                FormDataParseAll.COMPONENT_PARSE_DATE.strToJava(component.getValue()).toDateStr());
    }

    @Override
    public FormData toFormData(GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues component) {
        return FormData.text(component.getName(), component.getValue());
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
