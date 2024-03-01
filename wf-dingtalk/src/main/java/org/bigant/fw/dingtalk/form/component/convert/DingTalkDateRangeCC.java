package org.bigant.fw.dingtalk.form.component.convert;

import com.alibaba.fastjson2.JSONArray;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.wf.form.bean.FormComponent;
import org.bigant.wf.form.component.ComponentParseAll;
import org.bigant.wf.form.component.ComponentType;
import org.bigant.wf.form.component.bean.DateRangeComponent;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 钉钉日期区间类型转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class DingTalkDateRangeCC extends DingTalkBaseCC {

    @Override
    public Map<String, String> toOther(FormComponent component, String dingTalkUserId) {
        DateRangeComponent rangeComponent = ComponentParseAll.COMPONENT_PARSE_DATE_RANGE.toJava(component.getValue());
        String begin = rangeComponent.getDateFormat().getParse().format(rangeComponent.getBegin());
        String end = rangeComponent.getDateFormat().getParse().format(rangeComponent.getEnd());
        return toMap(component.getName(), JSONArray.toJSONString(new String[]{begin, end}));
    }

    @Override
    public FormComponent toComponent(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues component) {


        return FormComponent.text(component.getName(), component.getValue());
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
