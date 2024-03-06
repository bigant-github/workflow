package org.bigant.fw.lark.instances.form.convert;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.instances.form.FormDataConvert;
import org.bigant.wf.process.bean.ProcessDetail;

import java.util.HashMap;
import java.util.Map;

/**
 * 钉钉组件转换器基础功能
 *
 * @author galen
 * @date 2024/3/115:38
 */
@Slf4j
public abstract class LarkBaseFDC implements FormDataConvert<Map<String, Object>, LarkBaseFDC.FormItemConvert
        , LarkBaseFDC.ToOtherParam> {


    protected Map<String, Object> base(FormItemConvert component, String type, Object value) {
        HashMap<String, Object> map = new HashMap<>(3);
        map.put("id", component.getFormItem().getId());
        map.put("type", type);
        map.put("value", value);
        return map;
    }

    @Data
    @AllArgsConstructor
    public static class FormItemConvert {
        private FormDataItem formComponents;
        private ProcessDetail.FormItem formItem;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class ToOtherParam {
        private JSONObject formObj;
        private Map<String, ProcessDetail.FormItem> formDetailItemMap;
    }
}