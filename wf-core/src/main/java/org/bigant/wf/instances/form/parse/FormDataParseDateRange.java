package org.bigant.wf.instances.form.parse;

import com.alibaba.fastjson2.JSONObject;
import org.bigant.wf.instances.form.FormDataParse;
import org.bigant.wf.instances.form.ComponentType;
import org.bigant.wf.instances.form.databean.FormDataDateRange;

import java.time.format.DateTimeFormatter;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class FormDataParseDateRange implements FormDataParse<FormDataDateRange> {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public FormDataDateRange strToJava(String str) {
        return JSONObject.parseObject(str, FormDataDateRange.class);
    }

    @Override
    public String toStr(FormDataDateRange data) {
        return JSONObject.toJSONString(data);
    }

    @Override
    public void verify(FormDataDateRange data) {
        
    }

    @Override
    public ComponentType type() {
        return ComponentType.DATE_RANGE;
    }
}
