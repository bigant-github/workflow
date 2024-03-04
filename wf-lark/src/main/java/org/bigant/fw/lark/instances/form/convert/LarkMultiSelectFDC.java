package org.bigant.fw.lark.instances.form.convert;

import com.alibaba.fastjson2.JSONObject;
import org.bigant.fw.lark.LarkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.form.option.MultiSelectOption;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.instances.form.FormDataParseAll;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 钉钉多选框转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class LarkMultiSelectFDC extends LarkBaseFDC {

    @Override
    public Map<String, Object> toOther(LarkBaseFDC.FormItemConvert component) {

        Collection<String> list = FormDataParseAll
                .COMPONENT_PARSE_MULTI_SELECT
                .strToJava(component.getFormComponents().getValue());


        MultiSelectOption option = (MultiSelectOption) component.getFormItem().getOption();

        Map<String, String> optionMap = option.optionsToMap();

        return this.base(component, "checkboxV2", list.stream()
                .map(optionMap::get)
                .collect(Collectors.toList()));
    }

    @Override
    public FormDataItem toFormData(
            JSONObject data) {
        /*return FormData.multiSelect(data.getName(), JSONArray.parseArray(data.getValue(), String.class));*/
        return null;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.MULTI_SELECT;
    }

    @Override
    public Collection<String> getOtherType() {
        return LarkFormType.MULTI_SELECT.getLarkType();
    }
}
