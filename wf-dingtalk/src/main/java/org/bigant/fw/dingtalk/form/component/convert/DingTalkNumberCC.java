package org.bigant.fw.dingtalk.form.component.convert;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.wf.form.bean.FormComponent;
import org.bigant.wf.form.component.ComponentType;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 钉钉数字类型转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class DingTalkNumberCC extends DingTalkBaseCC {

    @Override
    public Map<String, String> toOther(FormComponent component, String dingTalkUserId) {
        return this.toOtherValue(component);
    }

    @Override
    public FormComponent toComponent(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues component) {
        return FormComponent.number(component.getName(), new BigDecimal(component.getValue()));
    }

    @Override
    public ComponentType getType() {
        return ComponentType.NUMBER;
    }

    @Override
    public Collection<String> getOtherType() {
        return Collections.singletonList("NumberField");
    }
}
