package org.bigant.fw.dingtalk.process.form.convert;

import com.aliyun.dingtalkworkflow_1_0.models.QuerySchemaByProcessCodeResponseBody;
import org.bigant.fw.dingtalk.DingTalkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.process.form.FormDetailItem;

import java.util.Collection;


/**
 * 钉钉单选框类型转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class DingTalkSelectFDTC extends DingTalkBaseFDTC {

    @Override
    public Object toOther(FormDetailItem data) {
        return null;
    }

    @Override
    public FormDataItem toFormData(
            QuerySchemaByProcessCodeResponseBody.QuerySchemaByProcessCodeResponseBodyResultSchemaContentItems data) {
        return null;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.SELECT;
    }

    @Override
    public Collection<String> getOtherType() {
        return DingTalkFormType.SELECT.getDingTalkType();
    }
}
