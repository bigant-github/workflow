package org.bigant.fw.dingtalk.instances.form.convert;

import com.alibaba.fastjson2.JSONArray;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.fw.dingtalk.DingTalkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.FormDataItem;

import java.util.Collection;
import java.util.Map;

/**
 * 钉钉多选框转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class DingTalkMultiSelectFDC extends DingTalkBaseFDC {

    @Override
    public Map<String, String> toOther(FormDataItem data, String dingTalkUserId) {
        return toOtherValue(data);
    }

    @Override
    public FormDataItem toFormData(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues data) {
        return FormDataItem.multiSelect(data.getName(), JSONArray.parseArray(data.getValue(), String.class));
    }

    @Override
    public ComponentType getType() {
        return ComponentType.MULTI_SELECT;
    }

    @Override
    public Collection<String> getOtherType() {
        return DingTalkFormType.MULTI_SELECT.getDingTalkType();
    }
}
