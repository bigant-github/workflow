package org.bigant.fw.lark.instances.form.convert;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.lark.LarkFile;
import org.bigant.fw.lark.LarkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.instances.form.FormDataParseAll;
import org.bigant.wf.instances.form.databean.FormDataAttachment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 钉钉附件类型转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
@Slf4j
@AllArgsConstructor
public class LarkAttachmentFDC extends LarkBaseFDC {

    private LarkFile larkFile;
    @Override
    public Map<String, Object> toOther(LarkBaseFDC.FormItemConvert component) {
        Collection<FormDataAttachment> formDataAttachments = FormDataParseAll
                .COMPONENT_PARSE_ATTACHMENT
                .strToJava(component.getFormComponents().getValue());

        List<String> list = new ArrayList<>();
        for (FormDataAttachment formDataAttachment : formDataAttachments) {
            String code = larkFile.uploadFile("attachment",
                    formDataAttachment.getName(),
                    formDataAttachment.getUrl(),
                    formDataAttachment.getSize());
            list.add(code);
        }

        return this.base(component, "attachmentV2", list);
    }

    @Override
    public FormDataItem toFormData(
            JSONObject data) {

        /*String value = data.getValue();
        JSONArray jsonVal = JSONArray.parse(value);
        ArrayList<FormDataAttachment> attachments = new ArrayList<>();
        for (int i = 0; i < jsonVal.size(); i++) {

            JSONObject jsonFile = jsonVal.getJSONObject(i);

            attachments
                    .add(FormDataAttachment.builder()
                            .name(jsonFile.getString("fileName"))
                            .size(jsonFile.getLong("fileSize"))
                            .build());

        }

        return FormData.attachment(data.getName(), attachments);*/
        return null;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.ATTACHMENT;
    }

    @Override
    public Collection<String> getOtherType() {
        return LarkFormType.ATTACHMENT.getLarkType();
    }


}
