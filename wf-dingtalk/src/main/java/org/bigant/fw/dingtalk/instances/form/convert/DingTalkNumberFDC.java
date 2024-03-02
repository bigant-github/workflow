package org.bigant.fw.dingtalk.instances.form.convert;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.wf.instances.form.FormData;
import org.bigant.wf.instances.form.ComponentType;

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
public class DingTalkNumberFDC extends DingTalkBaseFDC {

    @Override
    public Map<String, String> toOther(FormData component, String dingTalkUserId) {
        return this.toOtherValue(component);
    }

    @Override
    public FormData toFormData(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues component) {
        return FormData.number(component.getName(), new BigDecimal(component.getValue()));
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
