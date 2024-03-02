package org.bigant.wf.instances.form.parse;

import com.alibaba.fastjson2.JSONObject;
import org.bigant.wf.instances.form.FormDataParse;
import org.bigant.wf.instances.form.ComponentType;
import org.bigant.wf.instances.form.databean.AmountComponent;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class FormDataParseAmount implements FormDataParse<AmountComponent> {


    @Override
    public AmountComponent strToJava(String str) {
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
