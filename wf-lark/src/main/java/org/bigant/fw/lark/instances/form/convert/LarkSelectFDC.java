package org.bigant.fw.lark.instances.form.convert;

import com.alibaba.fastjson2.JSONObject;
import org.bigant.fw.lark.LarkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.form.option.SelectOption;
import org.bigant.wf.instances.form.FormDataItem;

import java.util.Collection;
import java.util.Map;

/**
 * 钉钉单选框类型转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class LarkSelectFDC extends LarkBaseFDC {

    @Override
    public Map<String, Object> toOther(LarkBaseFDC.FormItemConvert component) {

        SelectOption option = (SelectOption) component.getFormItem().getOption();

        Map<String, String> optionMap = option.optionsToMap();


        return this.base(component, "radioV2", optionMap.get(component.getFormComponents().getValue()));
    }

    @Override
    public FormDataItem toFormData(
            JSONObject data) {
        /*return FormData.select(data.getName(), data.getValue());*/
        return null;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.SELECT;
    }

    @Override
    public Collection<String> getOtherType() {
        return LarkFormType.SELECT.getLarkType();
    }
}
