package org.bigant.fw.dingtalk.form.component.convert;

import com.alibaba.fastjson2.JSONArray;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.wf.form.bean.FormComponent;
import org.bigant.wf.form.component.ComponentParseAll;
import org.bigant.wf.form.component.ComponentType;
import org.bigant.wf.form.component.bean.DateRangeComponent;
import org.bigant.wf.form.component.bean.ImageComponent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 钉钉图片类型转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class DingTalkImageCC extends DingTalkBaseCC {

    @Override
    public Map<String, String> toOther(FormComponent component, String dingTalkUserId) {
        List<ImageComponent> rangeComponent = ComponentParseAll.COMPONENT_PARSE_IMAGE.toJava(component.getValue());
        List<String> urls = rangeComponent
                .stream()
                .map(ImageComponent::getUrl)
                .collect(Collectors.toList());
        return toMap(component.getName(), JSONArray.toJSONString(urls));
    }

    @Override
    public FormComponent toComponent(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues component) {


        return FormComponent.text(component.getName(), component.getValue());
    }

    @Override
    public ComponentType getType() {
        return ComponentType.IMAGE;
    }

    @Override
    public Collection<String> getOtherType() {
        return Collections.singletonList("DDPhotoField");
    }
}
