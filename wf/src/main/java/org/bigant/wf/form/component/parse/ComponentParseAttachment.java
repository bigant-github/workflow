package org.bigant.wf.form.component.parse;

import com.alibaba.fastjson2.JSONArray;
import org.bigant.wf.form.component.ComponentParse;
import org.bigant.wf.form.component.ComponentType;
import org.bigant.wf.form.component.bean.Attachment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class ComponentParseAttachment implements ComponentParse<List<Attachment>> {


    @Override
    public List<Attachment> toJava(String str) {
        return JSONArray.parse(str).toJavaList(Attachment.class);
    }

    @Override
    public String toStr(List<Attachment> data) {
        return JSONArray.toJSONString(data);
    }

    @Override
    public void verify(List<Attachment> data) {

    }

    @Override
    public ComponentType type() {
        return ComponentType.ATTACHMENT;
    }
}
