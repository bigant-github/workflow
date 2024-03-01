package org.bigant.fw.dingtalk.form.component;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import org.bigant.fw.dingtalk.form.component.convert.DingTalkBaseCC;
import org.bigant.fw.dingtalk.form.component.convert.DingTalkTextCC;
import org.bigant.wf.form.component.ComponentConvertFactory;
import org.bigant.wf.form.component.ComponentType;

import java.util.HashMap;

/**
 * 钉钉组件转换器
 *
 * @author galen
 * @date 2024/3/115:27
 */
public class DingTalkCCF implements ComponentConvertFactory<String, GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues, DingTalkBaseCC> {


    public static final HashMap<ComponentType, DingTalkBaseCC> ccMap = new HashMap<>();

    static {
        ccMap.put(, new DingTalkTextCC());
    }

    @Override
    public DingTalkBaseCC text() {
        return null;
    }

    @Override
    public DingTalkBaseCC textarea() {
        return null;
    }

    @Override
    public DingTalkBaseCC select() {
        return null;
    }

    @Override
    public DingTalkBaseCC multiSelect() {
        return null;
    }

    @Override
    public DingTalkBaseCC date() {
        return null;
    }

    @Override
    public DingTalkBaseCC dateRange() {
        return null;
    }

    @Override
    public DingTalkBaseCC number() {
        return null;
    }

    @Override
    public DingTalkBaseCC amount() {
        return null;
    }

    @Override
    public DingTalkBaseCC image() {
        return null;
    }

    @Override
    public DingTalkBaseCC attachment() {
        return null;
    }

    @Override
    public DingTalkBaseCC unknown() {
        return null;
    }

    @Override
    public DingTalkBaseCC table() {
        return null;
    }
}
