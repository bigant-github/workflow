package org.bigant.fw.dingtalk.instances.form.convert;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.wf.instances.form.FormData;
import org.bigant.wf.instances.form.ComponentType;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 钉钉多选框转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class DingTalkMultiSelectFDC extends DingTalkBaseFDC {

    @Override
    public Map<String, String> toOther(FormData component, String dingTalkUserId) {
        return toOtherValue(component);
    }

    @Override
    public FormData toFormData(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues component) {
        return FormData.text(component.getName(), component.getValue());
    }

    @Override
    public ComponentType getType() {
        return ComponentType.MULTI_SELECT;
    }

    @Override
    public Collection<String> getOtherType() {
        return Collections.singletonList("DDMultiSelectField");
    }
}
