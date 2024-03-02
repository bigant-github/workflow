package org.bigant.fw.dingtalk.instances.form.convert;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import lombok.AllArgsConstructor;
import org.bigant.fw.dingtalk.instances.form.DingTalkFDCF;
import org.bigant.wf.instances.form.FormData;
import org.bigant.wf.form.option.AmountOption;
import org.bigant.wf.instances.form.FormDataParseAll;
import org.bigant.wf.instances.form.ComponentType;

import java.math.BigDecimal;
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
    public Map<String, String> toOther(FormData component, String dingTalkUserId) {
        Collection<Collection<FormData>> tableValue =
                FormDataParseAll.COMPONENT_PARSE_TABLE.strToJava(component.getValue());

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
        return toMap(component.getName(), table.toJSONString());
    }

    @Override
    public FormData toFormData(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues component) {

        return FormData.amount(component.getName(),
                new BigDecimal(component.getValue()),
                AmountOption.AmountType.CNY);
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
