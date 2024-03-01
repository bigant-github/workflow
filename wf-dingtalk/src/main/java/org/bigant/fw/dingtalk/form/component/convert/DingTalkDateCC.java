package org.bigant.fw.dingtalk.form.component.convert;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.wf.form.bean.FormComponent;
import org.bigant.wf.form.component.ComponentParseAll;
import org.bigant.wf.form.component.ComponentType;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 钉钉日期转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class DingTalkDateCC extends DingTalkBaseCC {

    @Override
    public Map<String, String> toOther(FormComponent component, String dingTalkUserId) {
        return toMap(component.getName(),
                ComponentParseAll.COMPONENT_PARSE_DATE.toJava(component.getValue()).toDateStr());
    }

    @Override
    public FormComponent toComponent(GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues component) {
        return FormComponent.text(component.getName(), component.getValue());
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
