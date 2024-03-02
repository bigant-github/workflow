package org.bigant.wf.instances.form.parse;

import com.alibaba.fastjson2.JSONArray;
import org.bigant.wf.instances.form.FormDataParse;
import org.bigant.wf.instances.form.ComponentType;

import java.util.List;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class FormDataParseMultiSelect implements FormDataParse<List<String>> {


    @Override
    public List<String> strToJava(String str) {
        return JSONArray.parseArray(str, String.class);
    }

    @Override
    public String toStr(List<String> data) {
        return JSONArray.toJSONString(data);
    }

    @Override
    public void verify(List<String> data) {

    }

    @Override
    public ComponentType type() {
        return ComponentType.MULTI_SELECT;
    }


}