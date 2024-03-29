package org.bigant.wf.process.form.option;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

public class DateOption {
    /**
     * 控件类型
     *
     * @author galen
     * date 2024/1/3115:33
     */
    @Getter
    @AllArgsConstructor
    public enum ComponentDateFormat {

        YYYY_MM_DD("年-月-日", DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        YYYY_MM_DD_HH_MM("年-月-日 时:分", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        String desc;

        DateTimeFormatter parse;

    }
}
