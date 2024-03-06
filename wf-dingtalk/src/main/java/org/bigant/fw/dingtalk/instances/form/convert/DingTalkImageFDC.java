package org.bigant.fw.dingtalk.instances.form.convert;

import com.alibaba.fastjson2.JSONArray;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.fw.dingtalk.DingTalkFormType;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.instances.form.FormDataParseAll;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.databean.FormDataImage;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 钉钉图片类型转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class DingTalkImageFDC extends DingTalkBaseFDC {

    @Override
    public Map<String, String> toOther(FormDataItem data, String dingTalkUserId) {
        Collection<FormDataImage> rangeComponent = FormDataParseAll.COMPONENT_PARSE_IMAGE.strToJava(data.getValue());
        List<String> urls = rangeComponent
                .stream()
                .map(FormDataImage::getUrl)
                .collect(Collectors.toList());
        return toMap(data.getName(), JSONArray.toJSONString(urls));
    }

    @Override
    public FormDataItem toFormData(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues data) {

        String value = data.getValue();
        JSONArray jsonVal = JSONArray.parse(value);
        ArrayList<FormDataImage> images = new ArrayList<>();
        for (int i = 0; i < jsonVal.size(); i++) {

            String filePath = jsonVal.getString(i);

            images
                    .add(FormDataImage.builder()
                            .url(filePath)
                            .build());

        }

        return FormDataItem.image(data.getName(), images);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.IMAGE;
    }

    @Override
    public Collection<String> getOtherType() {
        return DingTalkFormType.IMAGE.getDingTalkType();
    }
}
