package org.bigant.fw.lark;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 时间转换
 *
 * @author galen
 * date 2024/3/614:20
 */
public class DateUtil {

    public static LocalDateTime timestampToLocalDateTime(Long timestamp) {
        timestamp = timestamp * 1000;
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static LocalDateTime timestampToLocalDateTime(String timestamp) {
        return timestampToLocalDateTime(Long.valueOf(timestamp));
    }
}
