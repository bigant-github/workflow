package org.bigant.fw.lark.instances.form.convert;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import org.bigant.fw.lark.LarkFormType;
import org.bigant.fw.lark.instances.form.LarkFDCF;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.instances.form.FormDataParseAll;
import org.bigant.wf.process.bean.ProcessDetail;

import java.util.ArrayList;
import java.util.Collection;
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

        Collection<Collection<FormDataItem>> table = FormDataParseAll
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
    public FormDataItem toFormData(
            LarkBaseFDC.ToOtherParam data) {

        /*
         * {
         *     "id": "widget1",
         *     "name": "Item application",
         *     "type": "fieldList",
         *     "value": [
         *          [
         *             {
         *                 "id": "widget1",
         *                 "type": "checkbox",
         *                 "value": ["jxpsebqp-0"]
         *             }
         *          ]
         *      ]
         * }
         */
        JSONObject dataObj = data.getFormObj();
        JSONArray rowsObj = dataObj.getJSONArray("value");

        ArrayList<Collection<FormDataItem>> rows = new ArrayList<>(rowsObj.size());

        ProcessDetail.FormItem detailItem =
                data.getFormDetailItemMap()
                        .get(dataObj.getString("id"));

        Map<String, ProcessDetail.FormItem> collect = detailItem.getChildren().stream()
                .collect(Collectors.toMap(ProcessDetail.FormItem::getId, x -> x));

        for (int i = 0; i < rowsObj.size(); i++) {

            JSONArray row = rowsObj.getJSONArray(i);

            ArrayList<FormDataItem> fieldList = new ArrayList<>(row.size());

            for (int i1 = 0; i1 < row.size(); i1++) {
                /*
                 * {
                 *     "id": "widget1",
                 *     "type": "checkbox",
                 *     "value": ["jxpsebqp-0"]
                 * }
                 */
                JSONObject field = row.getJSONObject(i1);

                String type = field.getString("type");

                LarkBaseFDC fdc = dingTalkFDCF.getByOtherType(type);


                fieldList.add(
                        fdc.toFormData(
                                new ToOtherParam(field, collect)));
            }

            rows.add(fieldList);

        }


        return FormDataItem.table(data.getFormObj().getString("name"), rows);
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
