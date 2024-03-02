package org.bigant.wf.instances.form;

import lombok.extern.slf4j.Slf4j;
import org.bigant.wf.exception.WfException;

import java.util.HashMap;

/**
 * 表单数据转换器工厂
 * 改接口主要实现对所有表单数据转换器的管理
 * 所有审批流的实现都应该实现此接口来管理表单数据转换器，以达到添加类型后提醒到具体实现方
 *
 * @author galen
 * @date 2024/1/3115:33
 */
@Slf4j
public abstract class FormDataConvertFactory<R, P, T extends FormDataConvert<R, P>> {


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
            String errMsg = String.format("根据其他系统类型获取转换器失败。type:%s", type);
            log.info(errMsg);
            throw new WfException(errMsg);
        }

        return fdc;
    }

    protected abstract T text();

    protected abstract T textarea();

    protected abstract T select();

    protected abstract T multiSelect();

    protected abstract T date();

    protected abstract T dateRange();

    protected abstract T number();

    protected abstract T amount();

    protected abstract T image();

    protected abstract T attachment();

    protected abstract T table();

    protected abstract T unknown();

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
        formTypeMap.put(ComponentType.UNKNOWN, unknown());

        for (T cc : formTypeMap.values()) {
            for (String dingTalkType : cc.getOtherType()) {
                dingTalkTypeMap.put(dingTalkType, cc);
            }
        }
    }

}
