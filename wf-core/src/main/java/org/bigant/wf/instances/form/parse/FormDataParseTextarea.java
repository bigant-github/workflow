package org.bigant.wf.instances.form.parse;

import org.bigant.wf.instances.form.FormDataParse;
import org.bigant.wf.ComponentType;

/**
 * 组建工具
 *
 * @author galen
 * date 2024/1/3116:30
 */
public class FormDataParseTextarea implements FormDataParse<String> {

    @Override
    public String strToJava(String str) {
        return str;
    }

    @Override
    public String toStr(String data) {
        return data;
    }

    @Override
    public void verify(String data) {
        if (data != null && data.length() > 500) {
            throw new VerifyException("数据长度不能超过500");
        }
    }

    @Override
    public ComponentType type() {
        return ComponentType.TEXTAREA;
    }


}
