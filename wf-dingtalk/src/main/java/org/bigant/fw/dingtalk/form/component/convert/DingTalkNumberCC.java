package org.bigant.fw.dingtalk.form.component.convert;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.wf.form.bean.FormComponent;
import org.bigant.wf.form.component.ComponentType;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;

/**
 * 钉钉字符串转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class DingTalkNumberCC extends DingTalkBaseCC {

    @Override
    public String toOther(FormComponent component) {
        return component.getValue();
    }

    @Override
    public FormComponent toComponent(GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues component) {
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
