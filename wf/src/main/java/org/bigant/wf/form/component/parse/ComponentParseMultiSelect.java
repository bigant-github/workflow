package org.bigant.wf.form.component.parse;

import com.alibaba.fastjson2.JSONArray;
import org.bigant.wf.form.component.ComponentParse;
import org.bigant.wf.form.component.ComponentType;

import java.util.HashSet;
import java.util.Set;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class ComponentParseMultiSelect implements ComponentParse<Set<String>> {


    @Override
    public Set<String> toJava(String str) {
        return new HashSet<>(JSONArray.parseArray(str, String.class));
    }

    @Override
    public String toStr(Set<String> data) {
        return JSONArray.toJSONString(data);
    }

    @Override
    public void verify(Set<String> data) {

    }

    @Override
    public ComponentType type() {
        return ComponentType.MULTI_SELECT;
    }


}