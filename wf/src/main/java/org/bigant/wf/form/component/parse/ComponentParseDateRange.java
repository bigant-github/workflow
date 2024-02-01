package org.bigant.wf.form.component.parse;

import org.bigant.wf.form.component.ComponentParse;
import org.bigant.wf.form.component.ComponentType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class ComponentParseDateRange implements ComponentParse<LocalDate> {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public LocalDate toJava(String str) {
        return DATE_FORMAT.parse(str, LocalDate::from);
    }

    @Override
    public String toStr(LocalDate data) {
        return DATE_FORMAT.format(data);
    }

    @Override
    public void verify(LocalDate data) {
        
    }

    @Override
    public ComponentType type() {
        return ComponentType.DATE_RANGE;
    }
}
