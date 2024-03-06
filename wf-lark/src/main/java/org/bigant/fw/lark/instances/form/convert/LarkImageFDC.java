package org.bigant.fw.lark.instances.form.convert;

import com.alibaba.fastjson2.JSONArray;
import lombok.AllArgsConstructor;
import org.bigant.fw.lark.LarkFile;
import org.bigant.fw.lark.LarkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.FormDataItem;
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
    public FormDataItem toFormData(
            LarkBaseFDC.ToOtherParam data) {

        JSONArray value = data.getFormObj().getJSONArray("value");
        String[] ext = data.getFormObj().getString("ext").split(",");

        ArrayList<FormDataImage> attachments = new ArrayList<>();

        for (int i = 0; i < value.size(); i++) {

            attachments
                    .add(FormDataImage.builder()
                            .name(ext[i])
                            .url(value.getString(i))
                            .build());

        }

        return FormDataItem.image(data.getFormObj().getString("name"), attachments);
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
