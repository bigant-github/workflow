package org.bigant.wf.form.component.parse;

import org.bigant.wf.form.component.ComponentParse;
import org.bigant.wf.form.component.ComponentType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public class ComponentParseDate implements ComponentParse<LocalDateTime> {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public LocalDateTime toJava(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(str);
        return LocalDateTime.parse(str, formatter);
    }

    @Override
    public String toStr(LocalDateTime data) {
        return DATE_FORMAT.format(data);
    }

    @Override
    public void verify(LocalDateTime data) {

    }

    @Override
    public ComponentType type() {
        return ComponentType.DATE;
    }
}
