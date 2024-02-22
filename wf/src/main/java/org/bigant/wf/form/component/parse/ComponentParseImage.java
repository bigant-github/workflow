package org.bigant.wf.form.component.parse;

import com.alibaba.fastjson2.JSONArray;
import org.bigant.wf.form.component.ComponentParse;
import org.bigant.wf.form.component.ComponentType;
import org.bigant.wf.form.component.bean.AttachmentComponent;
import org.bigant.wf.form.component.bean.ImageComponent;

import java.util.List;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class ComponentParseImage implements ComponentParse<List<ImageComponent>> {


    @Override
    public List<ImageComponent> toJava(String str) {
        return JSONArray.parse(str).toJavaList(ImageComponent.class);
    }

    @Override
    public String toStr(List<ImageComponent> data) {
        return JSONArray.toJSONString(data);
    }

    @Override
    public void verify(List<ImageComponent> data) {

    }

    @Override
    public ComponentType type() {
        return ComponentType.ATTACHMENT;
    }
}
