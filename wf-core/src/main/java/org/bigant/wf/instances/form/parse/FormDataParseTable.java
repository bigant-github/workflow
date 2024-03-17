package org.bigant.wf.instances.form.parse;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.instances.form.FormDataParse;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * 组建工具
 *
 * @author galen
 * date 2024/1/3116:30
 */
public class FormDataParseTable implements FormDataParse<Collection<Collection<FormDataItem>>> {


    public final static Type TYPE = new TypeReference<List<List<FormDataItem>>>() {
    }.getType();

    @Override
    public Collection<Collection<FormDataItem>> strToJava(String str) {
        return JSONObject.parseObject(str, TYPE);
    }

    @Override
    public String toStr(Collection<Collection<FormDataItem>> data) {
        return JSONArray.toJSONString(data);
    }

    @Override
    public void verify(Collection<Collection<FormDataItem>> data) {

    }

    @Override
    public ComponentType type() {
        return ComponentType.TABLE;
    }
}
