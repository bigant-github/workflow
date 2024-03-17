package org.bigant.fw.lark.instances.form.convert;

import org.bigant.wf.ComponentType;
import org.bigant.wf.exception.WfException;
import org.bigant.wf.instances.form.FormDataItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 钉钉字符串转换器
 *
 * @author galen
 * date 2024/3/115:29
 */
public class LarkUnknownFDC extends LarkBaseFDC {


    @Override
    public Map<String, Object> toOther(LarkBaseFDC.FormItemConvert component) {
        String errorMsg = String.format("飞书-无法将%s转换为 form组件。", getType());
        throw new WfException(errorMsg);
    }

    @Override
    public FormDataItem toFormData(ToOtherParam component) {
        String errorMsg = String.format("飞书-无法将%s转换为 form组件。", getType());
        throw new WfException(errorMsg);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.UNKNOWN;
    }

    @Override
    public Collection<String> getOtherType() {
        return new ArrayList<>();
    }
}
