package org.bigant.wf.instances.form.parse;

import com.alibaba.fastjson2.JSONArray;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.FormDataParse;

import java.util.Collection;

/**
 * 关联审批单实体
 *
 * @author galen
 * date 2024/1/3116:30
 */
public class FormDataParseJoinInstance implements FormDataParse<Collection<String>> {

    @Override
    public Collection<String> strToJava(String str) {
        return JSONArray.parseArray(str, String.class);
    }

    @Override
    public String toStr(Collection<String> data) {
        return JSONArray.toJSONString(data);
    }

    @Override
    public void verify(Collection<String> data) {

    }

    @Override
    public ComponentType type() {
        return ComponentType.JOIN_INSTANCE;
    }


}
