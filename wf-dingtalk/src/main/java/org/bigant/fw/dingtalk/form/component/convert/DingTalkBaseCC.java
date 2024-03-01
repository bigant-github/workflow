package org.bigant.fw.dingtalk.form.component.convert;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.wf.exception.WfException;
import org.bigant.wf.form.bean.FormComponent;
import org.bigant.wf.form.component.ComponentConvert;

import java.util.HashMap;
import java.util.Map;

/**
 * 钉钉组件转换器基础功能
 *
 * @author galen
 * @date 2024/3/115:38
 */
public abstract class DingTalkBaseCC implements ComponentConvert<Map<String, String>
        , GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues> {


    @Override
    public Map<String, String> toOther(FormComponent component) {
        throw new WfException("钉钉-类型转换器暂不支持使用此方法，请使用toOther(FormComponent,String)方法。");
    }


    public abstract Map<String, String> toOther(FormComponent component, String dingTalkUserId);


    /**
     * 直接使用默认值
     *
     * @return
     */
    public Map<String, String> toOtherValue(FormComponent component) {
        HashMap<String, String> map = new HashMap<>(1);
        map.put(component.getName(), component.getValue());
        return map;
    }

    /**
     * 快速构建map
     *
     * @return
     */
    public Map<String, String> toMap(String name, String value) {
        HashMap<String, String> map = new HashMap<>(1);
        map.put(name, value);
        return map;
    }

}
