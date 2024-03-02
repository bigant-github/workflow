package org.bigant.wf.instances.form.parse;

import com.alibaba.fastjson2.JSONObject;
import org.bigant.wf.instances.form.FormDataParse;
import org.bigant.wf.instances.form.ComponentType;
import org.bigant.wf.instances.form.databean.DateRangeComponent;

import java.time.format.DateTimeFormatter;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class FormDataParseDateRange implements FormDataParse<DateRangeComponent> {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public DateRangeComponent strToJava(String str) {
        return JSONObject.parseObject(str, DateRangeComponent.class);
    }

    @Override
    public String toStr(DateRangeComponent data) {
        return JSONObject.toJSONString(data);
    }

    @Override
    public void verify(DateRangeComponent data) {
        
    }

    @Override
    public ComponentType type() {
        return ComponentType.DATE_RANGE;
    }
}
