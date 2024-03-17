package org.bigant.wf.instances.form.parse;

import com.alibaba.fastjson2.JSONArray;
import org.bigant.wf.instances.form.FormDataParse;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.databean.FormDataImage;

import java.util.Collection;

/**
 * 组建工具
 *
 * @author galen
 * date 2024/1/3116:30
 */
public class FormDataParseImage implements FormDataParse<Collection<FormDataImage>> {


    @Override
    public Collection<FormDataImage> strToJava(String str) {
        return JSONArray.parse(str).toJavaList(FormDataImage.class);
    }

    @Override
    public String toStr(Collection<FormDataImage> data) {
        return JSONArray.toJSONString(data);
    }

    @Override
    public void verify(Collection<FormDataImage> data) {

    }

    @Override
    public ComponentType type() {
        return ComponentType.ATTACHMENT;
    }
}
