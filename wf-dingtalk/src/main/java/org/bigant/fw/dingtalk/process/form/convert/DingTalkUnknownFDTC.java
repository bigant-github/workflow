package org.bigant.fw.dingtalk.process.form.convert;

import com.aliyun.dingtalkworkflow_1_0.models.QuerySchemaByProcessCodeResponseBody;
import org.bigant.wf.ComponentType;
import org.bigant.wf.exception.WfException;
import org.bigant.wf.process.form.FormDetailItem;

import java.util.Collection;
import java.util.Collections;

/**
 * 钉钉字符串转换器
 *
 * @author galen
 * @date 2024/3/115:29
 */
public class DingTalkUnknownFDTC extends DingTalkBaseFDTC {

    @Override
    public Object toOther(org.bigant.wf.process.form.FormDetailItem data) {
        String errorMsg = String.format("钉钉-无法将%s转换为 form组件。", getType());
        throw new WfException(errorMsg);
    }

    @Override
    public FormDetailItem toFormDetail(
            QuerySchemaByProcessCodeResponseBody.QuerySchemaByProcessCodeResponseBodyResultSchemaContentItems data) {
        String errorMsg = String.format("钉钉-无法将%s转换为 form组件。", getType());
        throw new WfException(errorMsg);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.UNKNOWN;
    }

    @Override
    public Collection<String> getOtherType() {
        return Collections.singleton("");
    }
}
