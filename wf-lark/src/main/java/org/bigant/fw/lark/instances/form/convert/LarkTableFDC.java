package org.bigant.fw.lark.instances.form.convert;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import org.bigant.fw.lark.LarkConfig;
import org.bigant.fw.lark.LarkFormType;
import org.bigant.fw.lark.instances.form.LarkFDCF;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.FormData;
import org.bigant.wf.instances.form.FormDataParseAll;
import org.bigant.wf.process.bean.ProcessDetail;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 钉钉数字类型转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
@AllArgsConstructor
public class LarkTableFDC extends LarkBaseFDC {

    private final LarkFDCF dingTalkFDCF;

    @Override
    public Map<String, Object> toOther(LarkBaseFDC.FormItemConvert component) {

        Collection<Collection<FormData>> table = FormDataParseAll
                .COMPONENT_PARSE_TABLE
                .strToJava(component.getFormComponents().getValue());

        List<ProcessDetail.FormItem> children = component.getFormItem().getChildren();
        Map<String, ProcessDetail.FormItem> childrenMap = children.stream()
                .collect(Collectors.toMap(ProcessDetail.FormItem::getName, x -> x));

        List<List<Map<String, Object>>> value = table.stream()
                .map(row -> row.stream()
                        .map(x -> dingTalkFDCF.getByFormType(x.getComponentType())
                                        .toOther(new LarkBaseFDC.FormItemConvert(x, childrenMap.get(x.getName()))))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        return this.base(component, "fieldList", value);
    }

    @Override
    public FormData toFormData(
            JSONObject data) {

        /*String value = data.getValue();
        JSONArray jsonVal = JSONArray.parse(value);

        ArrayList<Collection<FormData>> rows = new ArrayList<>(jsonVal.size());

        for (int i = 0; i < jsonVal.size(); i++) {
            JSONArray row = jsonVal.getJSONObject(i).getJSONArray("rowValue");
            ArrayList<FormData> fieldList = new ArrayList<>(row.size());

            for (int i1 = 0; i1 < row.size(); i1++) {
                JSONObject field = row.getJSONObject(i1);
                GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues model
                        = new GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues();

                String type = field.getString("key").split("_")[0];
                model.setName(field.getString("label"));
                model.setComponentType(type);
                model.setValue(field.getString("value"));

                LarkBaseFDC fdc = dingTalkFDCF.getByOtherType(type);
                fieldList.add(fdc.toFormData(model));
            }

            rows.add(fieldList);

        }


        return FormData.table(data.getName(), rows);*/
        return null;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.TABLE;
    }

    @Override
    public Collection<String> getOtherType() {
        return LarkFormType.TABLE.getLarkType();
    }
}
