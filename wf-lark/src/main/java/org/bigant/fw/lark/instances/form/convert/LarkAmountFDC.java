package org.bigant.fw.lark.instances.form.convert;

import org.bigant.fw.lark.LarkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.process.form.option.AmountOption;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.instances.form.FormDataParseAll;
import org.bigant.wf.instances.form.databean.FormDataAmount;

import java.util.Collection;
import java.util.Map;

/**
 * 钉钉数字类型转换器
 *
 * @author galen
 * date 2024/3/115:29
 */
public class LarkAmountFDC extends LarkBaseFDC {

    @Override
    public Map<String, Object> toOther(FormItemConvert data) {
        FormDataAmount formDataAmount = FormDataParseAll.COMPONENT_PARSE_AMOUNT.strToJava(data.getFormComponents().getValue());

        Map<String, Object> map = this.base(data, "amount", formDataAmount.getAmount());
        map.put("currency", "CNY");
        return map;
    }

    @Override
    public FormDataItem toFormData(
            LarkBaseFDC.ToOtherParam data) {

        return FormDataItem.amount(data.getFormObj().getString("name"),
                data.getFormObj().getBigDecimal("value"),
                AmountOption.AmountType.CNY);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.AMOUNT;
    }

    @Override
    public Collection<String> getOtherType() {
        return LarkFormType.AMOUNT.getLarkType();
    }
}
