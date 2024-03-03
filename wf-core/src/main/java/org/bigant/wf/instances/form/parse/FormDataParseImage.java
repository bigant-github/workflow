package org.bigant.wf.instances.form.parse;

import com.alibaba.fastjson2.JSONArray;
import org.bigant.wf.instances.form.FormDataParse;
import org.bigant.wf.instances.form.ComponentType;
import org.bigant.wf.instances.form.databean.FormDataImage;

import java.util.List;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class FormDataParseImage implements FormDataParse<List<FormDataImage>> {


    @Override
    public List<FormDataImage> strToJava(String str) {
        return JSONArray.parse(str).toJavaList(FormDataImage.class);
    }

    @Override
    public String toStr(List<FormDataImage> data) {
        return JSONArray.toJSONString(data);
    }

    @Override
    public void verify(List<FormDataImage> data) {

    }

    @Override
    public ComponentType type() {
        return ComponentType.ATTACHMENT;
    }
}
