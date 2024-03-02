package org.bigant.wf.instances.form.parse;

import com.alibaba.fastjson2.JSONObject;
import org.bigant.wf.instances.form.FormDataParse;
import org.bigant.wf.instances.form.ComponentType;
import org.bigant.wf.instances.form.databean.DateComponent;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class FormDataParseDate implements FormDataParse<DateComponent> {


    @Override
    public DateComponent strToJava(String str) {
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
