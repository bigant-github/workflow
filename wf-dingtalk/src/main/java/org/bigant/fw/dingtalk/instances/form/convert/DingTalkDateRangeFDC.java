package org.bigant.fw.dingtalk.instances.form.convert;

import com.alibaba.fastjson2.JSONArray;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.wf.instances.form.FormData;
import org.bigant.wf.instances.form.FormDataParseAll;
import org.bigant.wf.instances.form.ComponentType;
import org.bigant.wf.instances.form.databean.DateRangeComponent;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 钉钉日期区间类型转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class DingTalkDateRangeFDC extends DingTalkBaseFDC {

    @Override
    public Map<String, String> toOther(FormData component, String dingTalkUserId) {
        DateRangeComponent rangeComponent = FormDataParseAll.COMPONENT_PARSE_DATE_RANGE.strToJava(component.getValue());
        String begin = rangeComponent.getDateFormat().getParse().format(rangeComponent.getBegin());
        String end = rangeComponent.getDateFormat().getParse().format(rangeComponent.getEnd());
        return toMap(component.getName(), JSONArray.toJSONString(new String[]{begin, end}));
    }

    @Override
    public FormData toFormData(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues component) {


        return FormData.text(component.getName(), component.getValue());
    }

    @Override
    public ComponentType getType() {
        return ComponentType.DATE_RANGE;
    }

    @Override
    public Collection<String> getOtherType() {
        return Collections.singletonList("DDDateRangeField");
    }
}
