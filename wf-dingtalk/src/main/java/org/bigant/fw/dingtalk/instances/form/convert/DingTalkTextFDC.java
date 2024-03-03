package org.bigant.fw.dingtalk.instances.form.convert;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.FormData;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 钉钉字符串转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class DingTalkTextFDC extends DingTalkBaseFDC {

    @Override
    public Map<String, String> toOther(FormData data, String dingTalkUserId) {
        return toOtherValue(data);
    }

    @Override
    public FormData toFormData(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues data) {
        return FormData.text(data.getName(), data.getValue());
    }

    @Override
    public ComponentType getType() {
        return ComponentType.TEXT;
    }

    @Override
    public Collection<String> getOtherType() {
        return Collections.singletonList("TextField");
    }
}
