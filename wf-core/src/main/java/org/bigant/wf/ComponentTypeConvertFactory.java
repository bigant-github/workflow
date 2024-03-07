package org.bigant.wf;

import lombok.extern.slf4j.Slf4j;
import org.bigant.wf.exception.WfException;

import java.util.HashMap;

@Slf4j
public abstract class ComponentTypeConvertFactory<T extends ComponentTypeAndOtherType> implements ComponentTypeGet<T> {
    protected final HashMap<ComponentType, T> formTypeMap = new HashMap<>();
    protected final HashMap<String, T> dingTalkTypeMap = new HashMap<>();

    protected final boolean init = false;

    public T getByFormType(ComponentType type) {
        if (!init) {
            init();
        }
        T fdc = formTypeMap.get(type);

        if (fdc == null) {
            String errMsg = String.format("无法根据formType:%s 获取转换器。", type);
            log.error(errMsg);
            throw new WfException(errMsg);
        }
        return fdc;
    }


    /**
     * 根据其他系统类型获取转换器
     *
     * @param type
     * @return
     */
    public T getByOtherType(String type) {
        if (!init) {
            init();
        }
        T fdc = dingTalkTypeMap.get(type);

        if (fdc == null) {
            return getByFormType(ComponentType.UNKNOWN);
            /*String errMsg = String.format("根据其他系统类型获取转换器失败。type:%s", type);
            log.info(errMsg);
            throw new WfException(errMsg);*/
        }

        return fdc;
    }

    protected synchronized void init() {
        formTypeMap.put(ComponentType.TEXT, text());
        formTypeMap.put(ComponentType.TEXTAREA, textarea());
        formTypeMap.put(ComponentType.SELECT, select());
        formTypeMap.put(ComponentType.MULTI_SELECT, multiSelect());
        formTypeMap.put(ComponentType.DATE, date());
        formTypeMap.put(ComponentType.DATE_RANGE, dateRange());
        formTypeMap.put(ComponentType.NUMBER, number());
        formTypeMap.put(ComponentType.AMOUNT, amount());
        formTypeMap.put(ComponentType.IMAGE, image());
        formTypeMap.put(ComponentType.ATTACHMENT, attachment());
        formTypeMap.put(ComponentType.TABLE, table());
        formTypeMap.put(ComponentType.JOIN_INSTANCE, joinInstance());
        formTypeMap.put(ComponentType.UNKNOWN, unknown());

        for (T cc : formTypeMap.values()) {
            for (String dingTalkType : cc.getOtherType()) {
                dingTalkTypeMap.put(dingTalkType, cc);
            }
        }
    }


}
