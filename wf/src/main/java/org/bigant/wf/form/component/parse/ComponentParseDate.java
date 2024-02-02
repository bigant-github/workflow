package org.bigant.wf.form.component.parse;

import com.alibaba.fastjson2.JSONObject;
import org.bigant.wf.form.component.ComponentParse;
import org.bigant.wf.form.component.ComponentType;
import org.bigant.wf.form.component.bean.DateComponent;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class ComponentParseDate implements ComponentParse<DateComponent> {


    @Override
    public DateComponent toJava(String str) {
        return JSONObject.parseObject(str, DateComponent.class);
    }

    @Override
    public String toStr(DateComponent data) {
        return JSONObject.toJSONString(data);
    }

    @Override
    public void verify(DateComponent data) {

    }

    @Override
    public ComponentType type() {
        return ComponentType.DATE;
    }
}
