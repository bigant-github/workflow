package org.bigant.fw.lark.instances.form.convert;

import com.alibaba.fastjson2.JSONObject;
import org.bigant.fw.lark.LarkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.FormData;

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
    public FormData toFormData(
            JSONObject data) {
        /*return FormData.text(data.getName(), data.getValue());*/
        return null;
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
