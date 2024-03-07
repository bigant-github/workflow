package org.bigant.fw.lark.instances.form;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.lark.LarkConfig;
import org.bigant.fw.lark.LarkFile;
import org.bigant.fw.lark.instances.form.convert.*;
import org.bigant.wf.instances.form.FormDataConvertFactory;

import java.util.Map;

/**
 * 钉钉组件转换器
 *
 * @author galen
 * @date 2024/3/115:27
 */
@Slf4j
public class LarkFDCF
        extends FormDataConvertFactory<
        Map<String, Object>,
        LarkBaseFDC.FormItemConvert,
        LarkBaseFDC.ToOtherParam,
        LarkBaseFDC> {


    private final LarkFile larkFile;


    public LarkFDCF(
            LarkFile larkFile) {
        this.larkFile = larkFile;

    }

    @Override
    public LarkBaseFDC text() {
        return new LarkTextFDC();
    }

    @Override
    public LarkBaseFDC textarea() {
        return new LarkTextareaFDC();
    }

    @Override
    public LarkBaseFDC select() {
        return new LarkSelectFDC();
    }

    @Override
    public LarkBaseFDC multiSelect() {
        return new LarkMultiSelectFDC();
    }

    @Override
    public LarkBaseFDC date() {
        return new LarkDateFDC();
    }

    @Override
    public LarkBaseFDC dateRange() {
        return new LarkDateRangeFDC();
    }

    @Override
    public LarkBaseFDC number() {
        return new LarkNumberFDC();
    }

    @Override
    public LarkBaseFDC amount() {
        return new LarkAmountFDC();
    }

    @Override
    public LarkBaseFDC image() {
        return new LarkImageFDC(larkFile);
    }

    @Override
    public LarkBaseFDC attachment() {
        return new LarkAttachmentFDC(larkFile);
    }

    @Override
    public LarkBaseFDC table() {
        return new LarkTableFDC(this);
    }

    @Override
    public LarkBaseFDC joinInstance() {
        return new LarkJoinInstanceFDC();
    }

    @Override
    public LarkBaseFDC unknown() {
        return new LarkUnknownFDC();
    }


}
