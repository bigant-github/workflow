package org.bigant.wf.form.component.parse;

import com.alibaba.fastjson2.JSONObject;
import org.bigant.wf.form.component.ComponentParse;
import org.bigant.wf.form.component.ComponentType;
import org.bigant.wf.form.component.bean.AmountComponent;
import org.bigant.wf.form.component.bean.DateComponent;

import java.math.BigDecimal;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class ComponentParseAmount implements ComponentParse<AmountComponent> {


    @Override
    public AmountComponent toJava(String str) {
        return JSONObject.parseObject(str, AmountComponent.class);
    }

    @Override
    public String toStr(AmountComponent data) {
        return JSONObject.toJSONString(data);
    }

    @Override
    public void verify(AmountComponent data) {
    }

    @Override
    public ComponentType type() {
        return ComponentType.AMOUNT;
    }

}
