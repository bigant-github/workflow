package org.bigant.wf.instances.form.parse;

import com.alibaba.fastjson2.JSONArray;
import org.bigant.wf.instances.form.FormDataParse;
import org.bigant.wf.instances.form.ComponentType;
import org.bigant.wf.instances.form.databean.FormDataAttachment;

import java.util.List;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class FormDataParseAttachment implements FormDataParse<List<FormDataAttachment>> {


    @Override
    public List<FormDataAttachment> strToJava(String str) {
        return JSONArray.parse(str).toJavaList(FormDataAttachment.class);
    }

    @Override
    public String toStr(List<FormDataAttachment> data) {
        return JSONArray.toJSONString(data);
    }

    @Override
    public void verify(List<FormDataAttachment> data) {

    }

    @Override
    public ComponentType type() {
        return ComponentType.ATTACHMENT;
    }
}
