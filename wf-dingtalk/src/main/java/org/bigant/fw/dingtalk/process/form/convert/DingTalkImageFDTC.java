package org.bigant.fw.dingtalk.process.form.convert;

import com.aliyun.dingtalkworkflow_1_0.models.QuerySchemaByProcessCodeResponseBody;
import org.bigant.fw.dingtalk.DingTalkFormType;
import org.bigant.wf.ComponentType;
import org.bigant.wf.process.form.FormDetailItem;

import java.util.Collection;

/**
 * 钉钉图片类型转换器
 *
 * @author galen
 * date 2024/3/115:29
 */
public class DingTalkImageFDTC extends DingTalkBaseFDTC {

    @Override
    public Object toOther(org.bigant.wf.process.form.FormDetailItem data) {
        return null;
    }

    @Override
    public FormDetailItem toFormDetail(
            QuerySchemaByProcessCodeResponseBody.QuerySchemaByProcessCodeResponseBodyResultSchemaContentItems detail) {
        return convert(detail);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.IMAGE;
    }

    @Override
    public Collection<String> getOtherType() {
        return DingTalkFormType.IMAGE.getDingTalkType();
    }
}
