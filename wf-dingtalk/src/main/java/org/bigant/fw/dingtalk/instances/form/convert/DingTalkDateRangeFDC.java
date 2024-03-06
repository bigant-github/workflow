package org.bigant.fw.dingtalk.instances.form.convert;

import com.alibaba.fastjson2.JSONArray;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.fw.dingtalk.DingTalkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.process.form.option.DateOption;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.instances.form.FormDataParseAll;
import org.bigant.wf.instances.form.databean.FormDataDateRange;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 钉钉日期区间类型转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class DingTalkDateRangeFDC extends DingTalkBaseFDC {

    @Override
    public Map<String, String> toOther(FormDataItem data, String dingTalkUserId) {
        FormDataDateRange rangeComponent = FormDataParseAll.COMPONENT_PARSE_DATE_RANGE.strToJava(data.getValue());
        String begin = rangeComponent.getDateFormat().getParse().format(rangeComponent.getBegin());
        String end = rangeComponent.getDateFormat().getParse().format(rangeComponent.getEnd());
        return toMap(data.getName(), JSONArray.toJSONString(new String[]{begin, end}));
    }

    @Override
    public FormDataItem toFormData(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues data) {

        String value = data.getValue();
        String name = data.getName();

        List<String> jsonVal = JSONArray.parseArray(value,String.class);
        List<String> jsonName = JSONArray.parseArray(name,String.class);

        String beginDate = jsonVal.get(0);
        String endDate = jsonVal.get(1);

        String beginName = jsonName.get(0);
        String endName = jsonName.get(1);

        DateOption.ComponentDateFormat dateFormat = dateType(beginDate);


        return FormDataItem.dateRange(
                beginName, this.toLocalDateTime(beginDate, dateFormat),
                endName, this.toLocalDateTime(endDate, dateFormat),
                dateFormat);

    }

    @Override
    public ComponentType getType() {
        return ComponentType.DATE_RANGE;
    }

    @Override
    public Collection<String> getOtherType() {
        return DingTalkFormType.DATE_RANGE.getDingTalkType();
    }
}
