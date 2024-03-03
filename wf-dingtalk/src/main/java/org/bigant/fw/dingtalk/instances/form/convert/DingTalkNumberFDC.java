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
    public Map<String, String> toOther(FormData data, String dingTalkUserId) {
        return this.toOtherValue(data);
    }

    @Override
    public FormData toFormData(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues data) {
        return FormData.number(data.getName(), new BigDecimal(data.getValue()));
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
