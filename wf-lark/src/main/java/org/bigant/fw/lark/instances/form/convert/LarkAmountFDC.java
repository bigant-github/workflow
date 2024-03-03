package org.bigant.fw.lark.instances.form.convert;

import com.alibaba.fastjson2.JSONObject;
import org.bigant.fw.lark.LarkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.FormData;

import java.util.Collection;
import java.util.Map;

/**
 * 钉钉数字类型转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class LarkAmountFDC extends LarkBaseFDC {

    @Override
    public Map<String, Object> toOther(FormItemConvert data) {
        Map<String, Object> map = this.base(data, "amount", data.getFormComponents().getValue());
        map.put("currency", "CNY");
        return map;
    }

    @Override
    public FormData toFormData(
            JSONObject data) {

        /*return FormData.amount(data.getName(),
                new BigDecimal(data.getValue()),
                AmountOption.AmountType.CNY);*/
        return null;
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
