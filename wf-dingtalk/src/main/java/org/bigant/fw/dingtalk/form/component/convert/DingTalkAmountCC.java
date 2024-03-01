package org.bigant.fw.dingtalk.form.component.convert;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.wf.form.bean.FormComponent;
import org.bigant.wf.form.component.ComponentType;
import org.bigant.wf.form.component.bean.AmountComponent;

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
public class DingTalkAmountCC extends DingTalkBaseCC {

    @Override
    public Map<String, String> toOther(FormComponent component, String dingTalkUserId) {
        return this.toOtherValue(component);
    }

    @Override
    public FormComponent toComponent(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues component) {

        return FormComponent.amount(component.getName(),
                new BigDecimal(component.getValue()),
                AmountComponent.AmountType.CNY);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.AMOUNT;
    }

    @Override
    public Collection<String> getOtherType() {
        return Collections.singletonList("MoneyField");
    }
}
