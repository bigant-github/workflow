package org.bigant.wf.instances.form.parse;

import com.alibaba.fastjson2.JSONObject;
import org.bigant.wf.instances.form.FormDataParse;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.databean.FormDataDate;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class FormDataParseDate implements FormDataParse<FormDataDate> {


    @Override
    public FormDataDate strToJava(String str) {
        return JSONObject.parseObject(str, FormDataDate.class);
    }

    @Override
    public String toStr(FormDataDate data) {
        return JSONObject.toJSONString(data);
    }

    @Override
    public void verify(FormDataDate data) {

    }

    @Override
    public ComponentType type() {
        return ComponentType.DATE;
    }
}
