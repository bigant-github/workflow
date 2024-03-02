package org.bigant.wf.instances.form.parse;

import com.alibaba.fastjson2.JSONArray;
import org.bigant.wf.instances.form.FormDataParse;
import org.bigant.wf.instances.form.ComponentType;
import org.bigant.wf.instances.form.databean.AttachmentComponent;

import java.util.List;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class FormDataParseAttachment implements FormDataParse<List<AttachmentComponent>> {


    @Override
    public List<AttachmentComponent> strToJava(String str) {
        return JSONArray.parse(str).toJavaList(AttachmentComponent.class);
    }

    @Override
    public String toStr(List<AttachmentComponent> data) {
        return JSONArray.toJSONString(data);
    }

    @Override
    public void verify(List<AttachmentComponent> data) {

    }

    @Override
    public ComponentType type() {
        return ComponentType.ATTACHMENT;
    }
}
