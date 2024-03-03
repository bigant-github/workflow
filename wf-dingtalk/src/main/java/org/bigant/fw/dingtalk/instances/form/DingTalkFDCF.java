package org.bigant.fw.dingtalk.instances.form;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.dingtalk.DingTalkConfig;
import org.bigant.fw.dingtalk.DingTalkUser;
import org.bigant.fw.dingtalk.instances.form.convert.*;
import org.bigant.wf.cache.ICache;
import org.bigant.wf.instances.form.FormData;
import org.bigant.wf.instances.form.FormDataConvertFactory;

import java.util.Map;

/**
 * 钉钉组件转换器
 *
 * @author galen
 * @date 2024/3/115:27
 */
@Slf4j
public class DingTalkFDCF
        extends FormDataConvertFactory<
        Map<String, String>,
        FormData,
        GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues,
        DingTalkBaseFDC> {


    private final ICache cache;
    private final DingTalkConfig dingTalkConfig;
    private final DingTalkUser dingTalkUser;
    private com.aliyun.dingtalkworkflow_1_0.Client workflowClient;

    public DingTalkFDCF(
            DingTalkConfig dingTalkConfig,
            DingTalkUser dingTalkUser,
            ICache cache,
            com.aliyun.dingtalkworkflow_1_0.Client workflowClient) {
        this.cache = cache;
        this.dingTalkConfig = dingTalkConfig;
        this.dingTalkUser = dingTalkUser;
        this.workflowClient = workflowClient;

    }

    @Override
    public DingTalkBaseFDC text() {
        return new DingTalkTextFDC();
    }

    @Override
    public DingTalkBaseFDC textarea() {
        return new DingTalkTextareaFDC();
    }

    @Override
    public DingTalkBaseFDC select() {
        return new DingTalkSelectFDC();
    }

    @Override
    public DingTalkBaseFDC multiSelect() {
        return new DingTalkMultiSelectFDC();
    }

    @Override
    public DingTalkBaseFDC date() {
        return new DingTalkDateFDC();
    }

    @Override
    public DingTalkBaseFDC dateRange() {
        return new DingTalkDateRangeFDC();
    }

    @Override
    public DingTalkBaseFDC number() {
        return new DingTalkNumberFDC();
    }

    @Override
    public DingTalkBaseFDC amount() {
        return new DingTalkAmountFDC();
    }

    @Override
    public DingTalkBaseFDC image() {
        return new DingTalkImageFDC();
    }

    @Override
    public DingTalkBaseFDC attachment() {
        return new DingTalkAttachmentFDC(cache, dingTalkConfig, dingTalkUser, workflowClient);
    }

    @Override
    public DingTalkBaseFDC table() {
        return new DingTalkTableFDC(this);
    }

    @Override
    public DingTalkBaseFDC unknown() {
        return new DingTalkUnknownFDC();
    }


}
