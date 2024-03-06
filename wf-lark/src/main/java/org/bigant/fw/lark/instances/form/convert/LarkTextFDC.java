package org.bigant.fw.lark.instances.form.convert;

import com.alibaba.fastjson2.JSONObject;
import org.bigant.fw.lark.LarkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.form.option.AmountOption;
import org.bigant.wf.instances.form.FormDataItem;

import java.util.Collection;
import java.util.Map;

/**
 * 钉钉字符串转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class LarkTextFDC extends LarkBaseFDC {

    @Override
    public Map<String, Object> toOther(LarkBaseFDC.FormItemConvert component) {
        return this.base(component, "input", component.getFormComponents().getValue());
    }

    @Override
    public FormDataItem toFormData(
            LarkBaseFDC.ToOtherParam data) {
        return FormDataItem.text(data.getFormObj().getString("name"),
                data.getFormObj().getString("value"));
    }

    @Override
    public ComponentType getType() {
        return ComponentType.TEXT;
    }

    @Override
    public Collection<String> getOtherType() {
        return LarkFormType.TEXT.getLarkType();
    }
}
