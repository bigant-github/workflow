package org.bigant.fw.lark.instances.form.convert;

import com.alibaba.fastjson2.JSONObject;
import org.bigant.fw.lark.LarkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.FormData;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 钉钉数字类型转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class LarkNumberFDC extends LarkBaseFDC {

    @Override
    public Map<String, Object> toOther(LarkBaseFDC.FormItemConvert component) {
        return this.base(component, "number", component.getFormComponents().getValue());
    }

    public FormData toFormData(
            JSONObject data) {
        /*return FormData.number(data.getName(), new BigDecimal(data.getValue()));*/
        return null;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.NUMBER;
    }

    @Override
    public Collection<String> getOtherType() {
        return LarkFormType.NUMBER.getLarkType();
    }
}
