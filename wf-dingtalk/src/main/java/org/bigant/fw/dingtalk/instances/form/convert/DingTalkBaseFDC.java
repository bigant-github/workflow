package org.bigant.fw.dingtalk.instances.form.convert;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.bigant.wf.exception.WfException;
import org.bigant.wf.process.form.option.DateOption;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.instances.form.FormDataConvert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 钉钉组件转换器基础功能
 *
 * @author galen
 * date 2024/3/115:38
 */
@Slf4j
public abstract class DingTalkBaseFDC implements FormDataConvert<Map<String, String>, FormDataItem
        , GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues> {


    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Map<String, String> toOther(FormDataItem component) {
        throw new WfException("钉钉-类型转换器暂不支持使用此方法，请使用toOther(FormComponent,String)方法。");
    }


    public abstract Map<String, String> toOther(FormDataItem component, String dingTalkUserId);


    /**
     * 直接使用默认值
     *
     * @return
     */
    public Map<String, String> toOtherValue(FormDataItem component) {
        HashMap<String, String> map = new HashMap<>(1);
        map.put(component.getName(), component.getValue());
        return map;
    }

    /**
     * 快速构建map
     *
     * @return
     */
    public Map<String, String> toMap(String name, String value) {
        HashMap<String, String> map = new HashMap<>(1);
        map.put(name, value);
        return map;
    }

    /**
     * 日期格式
     *
     */
    public DateOption.ComponentDateFormat dateType(String dateStr) {
        int length = dateStr.length();
        //YYYY-MM-DD 格式
        if (length == 10) {
            return DateOption.ComponentDateFormat.YYYY_MM_DD;
        } else if (length == 16) {
            return DateOption.ComponentDateFormat.YYYY_MM_DD_HH_MM;
        } else {
            String errMsg = String.format("钉钉-无法识别的日期格式。data:%s", dateStr);
            log.error(errMsg);
            throw new WfException(errMsg);
        }

    }


    /**
     * 日期格式
     *
     */
    public LocalDateTime toLocalDateTime(String dateStr, DateOption.ComponentDateFormat format) {
        switch (format) {
            case YYYY_MM_DD:
                return LocalDateTime.parse(dateStr + " 00:00:00", DATE_TIME_FORMATTER);
            case YYYY_MM_DD_HH_MM:
                return LocalDateTime.parse(dateStr + ":00", DATE_TIME_FORMATTER);
            default:
                String errMsg = String.format("钉钉-无法识别的日期格式。date:%s", dateStr);
                log.error(errMsg);
                throw new WfException(errMsg);
        }

    }

}
