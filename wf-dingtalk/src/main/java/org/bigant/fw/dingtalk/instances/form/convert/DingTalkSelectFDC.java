package org.bigant.fw.dingtalk.instances.form.convert;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.ComponentType;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 钉钉单选框类型转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class DingTalkSelectFDC extends DingTalkBaseFDC {

    @Override
    public Map<String, String> toOther(FormDataItem data, String dingTalkUserId) {
        return toOtherValue(data);
    }

    @Override
    public FormDataItem toFormData(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues data) {
        return FormDataItem.select(data.getName(), data.getValue());
    }

    @Override
    public ComponentType getType() {
        return ComponentType.SELECT;
    }

    @Override
    public Collection<String> getOtherType() {
        return Collections.singletonList("DDSelectField");
    }
}
