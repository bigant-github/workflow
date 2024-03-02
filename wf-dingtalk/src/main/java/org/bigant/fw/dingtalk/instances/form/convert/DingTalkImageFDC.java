package org.bigant.fw.dingtalk.instances.form.convert;

import com.alibaba.fastjson2.JSONArray;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.wf.instances.form.FormData;
import org.bigant.wf.instances.form.FormDataParseAll;
import org.bigant.wf.instances.form.ComponentType;
import org.bigant.wf.instances.form.databean.ImageComponent;

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
public class DingTalkImageFDC extends DingTalkBaseFDC {

    @Override
    public Map<String, String> toOther(FormData component, String dingTalkUserId) {
        List<ImageComponent> rangeComponent = FormDataParseAll.COMPONENT_PARSE_IMAGE.strToJava(component.getValue());
        List<String> urls = rangeComponent
                .stream()
                .map(ImageComponent::getUrl)
                .collect(Collectors.toList());
        return toMap(component.getName(), JSONArray.toJSONString(urls));
    }

    @Override
    public FormData toFormData(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues component) {


        return FormData.text(component.getName(), component.getValue());
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
