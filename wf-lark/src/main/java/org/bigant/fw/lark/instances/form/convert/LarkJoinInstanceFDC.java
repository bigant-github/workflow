package org.bigant.fw.lark.instances.form.convert;

import org.bigant.fw.lark.LarkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.instances.form.FormDataParseAll;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 钉钉字符串转换器
 *
 * @author galen
 * date 2024/3/115:29
 */
public class LarkJoinInstanceFDC extends LarkBaseFDC {

    @Override
    public Map<String, Object> toOther(FormItemConvert component) {
        return this.base(component,
                "connect",
                FormDataParseAll.COMPONENT_PARSE_JOIN_INSTANCE
                        .strToJava(component.getFormComponents().getValue()));
    }

    @Override
    public FormDataItem toFormData(
            ToOtherParam data) {
        List<String> value = data.getFormObj().getList("value", String.class);
        return FormDataItem.joinInstance(data.getFormObj().getString("name"), value);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.JOIN_INSTANCE;
    }

    @Override
    public Collection<String> getOtherType() {
        return LarkFormType.JOIN_INSTANCE.getLarkType();
    }
}
