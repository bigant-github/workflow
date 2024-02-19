package org.bigant.wf.form.component.parse;

import org.bigant.wf.form.component.ComponentType;
import org.bigant.wf.form.component.ComponentParse;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class ComponentParseSelect implements ComponentParse<String> {

    @Override
    public String toJava(String str) {
        return str;
    }

    @Override
    public String toStr(String data) {
        return data;
    }

    @Override
    public void verify(String data) {
        if (data != null && data.length() > 100) {
            throw new ComponentParseMultiSelect.VerifyException("数据长度不能超过100");
        }
    }

    @Override
    public ComponentType type() {
        return ComponentType.SELECT;
    }


}