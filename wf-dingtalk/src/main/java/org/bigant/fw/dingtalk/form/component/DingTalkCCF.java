package org.bigant.fw.dingtalk.form.component;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.bigant.fw.dingtalk.DingTalkConfig;
import org.bigant.fw.dingtalk.DingTalkUser;
import org.bigant.fw.dingtalk.form.component.convert.*;
import org.bigant.wf.cache.ICache;
import org.bigant.wf.exception.WfException;
import org.bigant.wf.form.component.ComponentConvertFactory;
import org.bigant.wf.form.component.ComponentType;

import java.util.HashMap;
import java.util.Map;

/**
 * 钉钉组件转换器
 *
 * @author galen
 * @date 2024/3/115:27
 */
@Slf4j
public class DingTalkCCF
        implements ComponentConvertFactory<
        Map<String, String>,
        GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues,
        DingTalkBaseCC> {


    public final HashMap<ComponentType, DingTalkBaseCC> formTypeMap = new HashMap<>();
    public final HashMap<String, DingTalkBaseCC> dingTalkTypeMap = new HashMap<>();
    private final ICache cache;
    private final DingTalkConfig dingTalkConfig;
    private final DingTalkUser dingTalkUser;

    private com.aliyun.dingtalkworkflow_1_0.Client client;

    public DingTalkCCF(
            DingTalkConfig dingTalkConfig,
            DingTalkUser dingTalkUser,
            ICache cache,
            com.aliyun.dingtalkworkflow_1_0.Client client) {
        this.cache = cache;
        this.dingTalkConfig = dingTalkConfig;
        this.dingTalkUser = dingTalkUser;
        this.client = client;
        formTypeMap.put(ComponentType.TEXT, new DingTalkTextCC());
        formTypeMap.put(ComponentType.TEXTAREA, new DingTalkTextareaCC());
        formTypeMap.put(ComponentType.SELECT, new DingTalkSelectCC());
        formTypeMap.put(ComponentType.MULTI_SELECT, new DingTalkMultiSelectCC());
        formTypeMap.put(ComponentType.DATE, new DingTalkDateCC());
        formTypeMap.put(ComponentType.DATE_RANGE, new DingTalkDateRangeCC());
        formTypeMap.put(ComponentType.NUMBER, new DingTalkNumberCC());
        formTypeMap.put(ComponentType.AMOUNT, new DingTalkAmountCC());
        formTypeMap.put(ComponentType.IMAGE, new DingTalkImageCC());
        formTypeMap.put(ComponentType.ATTACHMENT, new DingTalkAttachmentCC(cache, dingTalkConfig, dingTalkUser, client));
        formTypeMap.put(ComponentType.TABLE, new DingTalkTableCC(this));
        formTypeMap.put(ComponentType.UNKNOWN, new DingTalkUnknownCC());

        for (DingTalkBaseCC cc : formTypeMap.values()) {
            for (String dingTalkType : cc.getOtherType()) {
                dingTalkTypeMap.put(dingTalkType, cc);
            }
        }

    }

    @Override
    public DingTalkBaseCC text() {
        return formTypeMap.get(ComponentType.TEXT);
    }

    @Override
    public DingTalkBaseCC textarea() {
        return formTypeMap.get(ComponentType.TEXTAREA);
    }

    @Override
    public DingTalkBaseCC select() {
        return formTypeMap.get(ComponentType.SELECT);
    }

    @Override
    public DingTalkBaseCC multiSelect() {
        return formTypeMap.get(ComponentType.MULTI_SELECT);
    }

    @Override
    public DingTalkBaseCC date() {
        return formTypeMap.get(ComponentType.DATE);
    }

    @Override
    public DingTalkBaseCC dateRange() {
        return formTypeMap.get(ComponentType.DATE_RANGE);
    }

    @Override
    public DingTalkBaseCC number() {
        return formTypeMap.get(ComponentType.NUMBER);
    }

    @Override
    public DingTalkBaseCC amount() {
        return formTypeMap.get(ComponentType.AMOUNT);
    }

    @Override
    public DingTalkBaseCC image() {
        return formTypeMap.get(ComponentType.IMAGE);
    }

    @Override
    public DingTalkBaseCC attachment() {
        return formTypeMap.get(ComponentType.ATTACHMENT);
    }

    @Override
    public DingTalkBaseCC table() {
        return formTypeMap.get(ComponentType.TABLE);
    }

    @Override
    public DingTalkBaseCC unknown() {
        return formTypeMap.get(ComponentType.UNKNOWN);
    }

    /**
     * 根据钉钉类型获取转换器
     *
     * @param dingTalkType
     * @return
     */
    public DingTalkBaseCC getCCByDingTalkType(String dingTalkType) {
        DingTalkBaseCC cc = dingTalkTypeMap.get(dingTalkType);

        if (cc == null) {
            String errMsg = String.format("钉钉-根据钉钉类型获取转换器失败。dingTalkType:%s", dingTalkType);
            log.info(errMsg);
            throw new WfException(errMsg);
        }

        return cc;
    }
}
