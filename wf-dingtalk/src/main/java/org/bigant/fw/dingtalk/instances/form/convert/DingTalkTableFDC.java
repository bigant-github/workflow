package org.bigant.fw.dingtalk.instances.form.convert;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import lombok.AllArgsConstructor;
import org.bigant.fw.dingtalk.instances.form.DingTalkFDCF;
import org.bigant.wf.instances.form.ComponentType;
import org.bigant.wf.instances.form.FormData;
import org.bigant.wf.instances.form.FormDataParseAll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 钉钉数字类型转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
@AllArgsConstructor
public class DingTalkTableFDC extends DingTalkBaseFDC {

    private final DingTalkFDCF dingTalkFDCF;

    @Override
    public Map<String, String> toOther(FormData data, String dingTalkUserId) {
        Collection<Collection<FormData>> tableValue =
                FormDataParseAll.COMPONENT_PARSE_TABLE.strToJava(data.getValue());

        JSONArray table = new JSONArray();
        for (Collection<FormData> componentList : tableValue) {

            JSONArray row = new JSONArray();
            for (FormData child : componentList) {
                Map<String, String> value = dingTalkFDCF.getByFormType(child.getComponentType()).toOther(child, dingTalkUserId);

                for (Map.Entry<String, String> entry : value.entrySet()) {
                    JSONObject json = new JSONObject();
                    json.put("name", entry.getKey());
                    json.put("value", entry.getValue());
                    row.add(json);
                }

            }

            table.add(row);
        }
        return toMap(data.getName(), table.toJSONString());
    }

    @Override
    public FormData toFormData(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues data) {

        String value = data.getValue();
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

                DingTalkBaseFDC fdc = dingTalkFDCF.getByOtherType(type);
                fieldList.add(fdc.toFormData(model));
            }

            rows.add(fieldList);

        }


        return FormData.table(data.getName(), rows);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.TABLE;
    }

    @Override
    public Collection<String> getOtherType() {
        return Collections.singletonList("TableField");
    }
}
