package org.bigant.fw.dingtalk.form.component.convert;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import lombok.AllArgsConstructor;
import org.bigant.fw.dingtalk.form.component.DingTalkCCF;
import org.bigant.wf.form.bean.FormComponent;
import org.bigant.wf.form.component.ComponentParseAll;
import org.bigant.wf.form.component.ComponentType;
import org.bigant.wf.form.component.bean.AmountComponent;

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
public class DingTalkTableCC extends DingTalkBaseCC {

    private final DingTalkCCF dingTalkCCF;

    @Override
    public Map<String, String> toOther(FormComponent component, String dingTalkUserId) {
        Collection<Collection<FormComponent>> tableValue =
                ComponentParseAll.COMPONENT_PARSE_TABLE.toJava(component.getValue());

        JSONArray table = new JSONArray();
        for (Collection<FormComponent> componentList : tableValue) {

            JSONArray row = new JSONArray();
            for (FormComponent child : componentList) {
                Map<String, String> value = dingTalkCCF.getConvert(child.getComponentType()).toOther(child, dingTalkUserId);

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
    public FormComponent toComponent(
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues component) {

        return FormComponent.amount(component.getName(),
                new BigDecimal(component.getValue()),
                AmountComponent.AmountType.CNY);
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
