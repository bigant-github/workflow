package org.bigant.wf.form.component.parse;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import org.bigant.wf.form.component.ComponentType;
import org.bigant.wf.form.bean.FormComponent;
import org.bigant.wf.form.component.ComponentParse;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class ComponentParseTable implements ComponentParse<Collection<Collection<FormComponent>>> {


    public final static Type TYPE = new TypeReference<List<List<FormComponent>>>() {
    }.getType();

    @Override
    public Collection<Collection<FormComponent>> toJava(String str) {
        return JSONObject.parseObject(str, TYPE);
    }

    @Override
    public String toStr(Collection<Collection<FormComponent>> data) {
        return JSONArray.toJSONString(data);
    }

    @Override
    public void verify(Collection<Collection<FormComponent>> data) {

    }

    @Override
    public ComponentType type() {
        return ComponentType.TABLE;
    }
}
