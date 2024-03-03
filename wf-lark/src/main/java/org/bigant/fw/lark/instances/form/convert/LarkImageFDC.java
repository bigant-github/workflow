package org.bigant.fw.lark.instances.form.convert;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import org.bigant.fw.lark.LarkFile;
import org.bigant.fw.lark.LarkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.FormData;
import org.bigant.wf.instances.form.FormDataParseAll;
import org.bigant.wf.instances.form.databean.FormDataImage;

import java.util.*;

/**
 * 钉钉图片类型转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
@AllArgsConstructor
public class LarkImageFDC extends LarkBaseFDC {

    LarkFile larkFile;
    @Override
    public Map<String, Object> toOther(FormItemConvert component) {


        Collection<FormDataImage> formDataImages = FormDataParseAll
                .COMPONENT_PARSE_IMAGE
                .strToJava(component.getFormComponents().getValue());

        List<String> list = new ArrayList<>();
        for (FormDataImage formDataImage : formDataImages) {

            String code = larkFile.uploadFile("image",
                    formDataImage.getName(),
                    formDataImage.getUrl(),
                    formDataImage.getSize());
            list.add(code);

        }

        return this.base(component, "image", list);
    }

    @Override
    public FormData toFormData(
            JSONObject data) {

        /*String value = data.getValue();
        JSONArray jsonVal = JSONArray.parse(value);
        ArrayList<FormDataImage> images = new ArrayList<>();
        for (int i = 0; i < jsonVal.size(); i++) {

            String filePath = jsonVal.getString(i);

            images
                    .add(FormDataImage.builder()
                            .url(filePath)
                            .build());

        }

        return FormData.image(data.getName(), images);*/

        return null;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.IMAGE;
    }

    @Override
    public Collection<String> getOtherType() {
        return LarkFormType.IMAGE.getLarkType();
    }
}
