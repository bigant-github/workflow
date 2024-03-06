package org.bigant.fw.lark.instances.form.convert;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.bigant.fw.lark.LarkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.form.option.DateOption;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.instances.form.FormDataParseAll;
import org.bigant.wf.instances.form.databean.FormDataDateRange;
import org.bigant.wf.process.form.FormDetailItem;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 钉钉日期区间类型转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class LarkDateRangeFDC extends LarkBaseFDC {

    @Override
    public Map<String, Object> toOther(LarkBaseFDC.FormItemConvert component) {
        FormDataDateRange dateRange = FormDataParseAll
                .COMPONENT_PARSE_DATE_RANGE
                .strToJava(component.getFormComponents().getValue());
        String begin = dateRange.getBegin().atOffset(ZoneOffset.ofHours(8))
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String end = dateRange.getEnd().atOffset(ZoneOffset.ofHours(8))
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        HashMap<String, Object> map = new HashMap<>();
        map.put("start", begin);
        map.put("end", end);

        switch (dateRange.getDateFormat()) {
            case YYYY_MM_DD:
                map.put("interval",
                        Duration.between(dateRange.getBegin().toLocalDate()
                                , dateRange.getEnd().toLocalDate()).toDays());

            case YYYY_MM_DD_HH_MM:

                map.put("interval",
                        Duration.between(
                                dateRange.getBegin().withSecond(0),
                                dateRange.getEnd().withSecond(0)).toHours());
        }

        return this.base(component, "dateInterval", map);

    }

    @Override
    public FormDataItem toFormData(LarkBaseFDC.ToOtherParam data) {

        JSONObject dataObj = data.getFormObj();
        JSONObject value = dataObj.getJSONObject("value");
        FormDetailItem detailItem = data.getFormDetailItemMap()
                .get(dataObj.getString("id"));


        String beginDate = value.getString("start");
        String endDate = value.getString("end");

        JSONArray nameArr = JSONArray.parse(detailItem.getName());

        return FormDataItem.dateRange(
                nameArr.getString(0), LocalDateTime.parse(beginDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                nameArr.getString(1), LocalDateTime.parse(endDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                DateOption.ComponentDateFormat.YYYY_MM_DD_HH_MM);

    }

    @Override
    public ComponentType getType() {
        return ComponentType.DATE_RANGE;
    }

    @Override
    public Collection<String> getOtherType() {
        return LarkFormType.DATE_RANGE.getLarkType();
    }
}
