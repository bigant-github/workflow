package org.bigant.fw.dingtalk.process.form.convert;

import com.aliyun.dingtalkworkflow_1_0.models.QuerySchemaByProcessCodeResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.bigant.wf.process.form.FormDetailConvert;
import org.bigant.wf.process.form.FormDetailItem;

/**
 * 钉钉组件转换器基础功能
 *
 * @author galen
 * date 2024/3/115:38
 */
@Slf4j
public abstract class DingTalkBaseFDTC implements FormDetailConvert<Object, org.bigant.wf.process.form.FormDetailItem
        , QuerySchemaByProcessCodeResponseBody.QuerySchemaByProcessCodeResponseBodyResultSchemaContentItems> {


    protected FormDetailItem convert(
            QuerySchemaByProcessCodeResponseBody.QuerySchemaByProcessCodeResponseBodyResultSchemaContentItems detail) {
        return FormDetailItem
                .builder()
                .name(detail.getProps().getLabel())
                .type(getType())
                .build();
    }


}
