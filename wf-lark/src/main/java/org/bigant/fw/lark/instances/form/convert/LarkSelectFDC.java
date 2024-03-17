package org.bigant.fw.lark.instances.form.convert;

import org.bigant.fw.lark.LarkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.process.form.option.SelectOption;
import org.bigant.wf.instances.form.FormDataItem;

import java.util.Collection;
import java.util.Map;

/**
 * 钉钉单选框类型转换器
 *
 * @author galen
 * date 2024/3/115:29
 */
public class LarkSelectFDC extends LarkBaseFDC {

    @Override
    public Map<String, Object> toOther(LarkBaseFDC.FormItemConvert component) {

        SelectOption option = (SelectOption) component.getFormDetailItem().getOption();

        Map<String, String> optionMap = option.optionsToMap();


        return this.base(component, "radioV2", optionMap.get(component.getFormComponents().getValue()));
    }

    @Override
    public FormDataItem toFormData(
            LarkBaseFDC.ToOtherParam data) {
        return FormDataItem.select(data.getFormObj().getString("name"),
                data.getFormObj().getString("value"));
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
