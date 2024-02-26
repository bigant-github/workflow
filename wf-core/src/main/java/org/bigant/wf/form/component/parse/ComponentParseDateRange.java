package org.bigant.wf.form.component.parse;

import com.alibaba.fastjson2.JSONObject;
import org.bigant.wf.form.component.ComponentParse;
import org.bigant.wf.form.component.ComponentType;
import org.bigant.wf.form.component.bean.DateRangeComponent;

import java.time.format.DateTimeFormatter;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class ComponentParseDateRange implements ComponentParse<DateRangeComponent> {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public DateRangeComponent toJava(String str) {
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
