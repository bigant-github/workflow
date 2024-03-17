package org.bigant.fw.dingtalk.instances.form.convert;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.fw.dingtalk.DingTalkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.FormDataItem;

import java.util.Collection;
import java.util.Map;

/**
 * 钉钉关联审批单
 *
 * @author galen
 * date 2024/3/115:29
 */
public class DingTalkJoinInstanceFDC extends DingTalkBaseFDC {

    @Override
    public Map<String, String> toOther(FormDataItem data, String dingTalkUserId) {
        return toOtherValue(data);
    }

    @Override
    public FormDataItem toFormData(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues data) {
        return FormDataItem.joinInstance(data.getName(), data.getValue());
    }

    @Override
    public ComponentType getType() {
        return ComponentType.JOIN_INSTANCE;
    }

    @Override
    public Collection<String> getOtherType() {
        return DingTalkFormType.JOIN_INSTANCE.getDingTalkType();
    }
}
