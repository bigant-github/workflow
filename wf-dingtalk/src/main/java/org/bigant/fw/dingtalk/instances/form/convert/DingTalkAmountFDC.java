package org.bigant.fw.dingtalk.instances.form.convert;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.wf.instances.form.FormData;
import org.bigant.wf.form.option.AmountOption;
import org.bigant.wf.instances.form.FormDataParseAll;
import org.bigant.wf.instances.form.ComponentType;
import org.bigant.wf.instances.form.databean.AmountComponent;

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
public class DingTalkAmountFDC extends DingTalkBaseFDC {

    @Override
    public Map<String, String> toOther(FormData component, String dingTalkUserId) {
        AmountComponent value =
                FormDataParseAll.COMPONENT_PARSE_AMOUNT.strToJava(component.getValue());
        return this.toMap(component.getName(), value.getAmount().toString());
    }

    @Override
    public FormData toFormData(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues component) {

        return FormData.amount(component.getName(),
                new BigDecimal(component.getValue()),
                AmountOption.AmountType.CNY);
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
