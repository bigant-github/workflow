package org.bigant.wf.form.component.parse;

import org.bigant.wf.form.component.ComponentParse;
import org.bigant.wf.form.component.ComponentType;

import java.math.BigDecimal;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class ComponentParseNumber implements ComponentParse<Number> {


    @Override
    public Number toJava(String str) {
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
