package org.bigant.fw.dingtalk.process.form;

import com.aliyun.dingtalkworkflow_1_0.models.QuerySchemaByProcessCodeResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.dingtalk.process.form.convert.*;
import org.bigant.wf.process.form.FormDetailConvertFactory;
import org.bigant.wf.process.form.FormDetailItem;

/**
 * 钉钉组件转换器
 *
 * @author galen
 * @date 2024/3/115:27
 */
@Slf4j
public class DingTalkFDTCF
        extends FormDetailConvertFactory<
                Object,
                FormDetailItem,
                QuerySchemaByProcessCodeResponseBody.QuerySchemaByProcessCodeResponseBodyResultSchemaContentItems,
        DingTalkBaseFDTC> {


    @Override
    public DingTalkBaseFDTC text() {
        return new DingTalkTextFDTC();
    }

    @Override
    public DingTalkBaseFDTC textarea() {
        return new DingTalkTextareaFDTC();
    }

    @Override
    public DingTalkBaseFDTC select() {
        return new DingTalkSelectFDTC();
    }

    @Override
    public DingTalkBaseFDTC multiSelect() {
        return new DingTalkMultiSelectFDTC();
    }

    @Override
    public DingTalkBaseFDTC date() {
        return new DingTalkDateFDTC();
    }

    @Override
    public DingTalkBaseFDTC dateRange() {
        return new DingTalkDateRangeFDTC();
    }

    @Override
    public DingTalkBaseFDTC number() {
        return new DingTalkNumberFDTC();
    }

    @Override
    public DingTalkBaseFDTC amount() {
        return new DingTalkAmountFDTC();
    }

    @Override
    public DingTalkBaseFDTC image() {
        return new DingTalkImageFDTC();
    }

    @Override
    public DingTalkBaseFDTC attachment() {
        return new DingTalkAttachmentFDTC();
    }

    @Override
    public DingTalkBaseFDTC table() {
        return new DingTalkTableFDTC(this);
    }

    @Override
    public DingTalkBaseFDTC joinInstance() {
        return null;
    }

    @Override
    public DingTalkBaseFDTC unknown() {
        return new DingTalkUnknownFDTC();
    }


}
