package org.bigant.wf.instances.form.parse;

import org.bigant.wf.instances.form.FormDataParse;
import org.bigant.wf.ComponentType;

import java.math.BigDecimal;

/**
 * 组建工具
 *
 * @author galen
 * date 2024/1/3116:30
 */
public class FormDataParseNumber implements FormDataParse<Number> {


    @Override
    public Number strToJava(String str) {
        return new BigDecimal(str);
    }

    @Override
    public String toStr(Number data) {
        return data.toString();
    }

    @Override
    public void verify(Number data) {
    }

    @Override
    public ComponentType type() {
        return ComponentType.NUMBER;
    }

}
