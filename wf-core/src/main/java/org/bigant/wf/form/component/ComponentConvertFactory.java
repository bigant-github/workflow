package org.bigant.wf.form.component;

import org.bigant.wf.exception.WfException;

/**
 * 控件转换器工厂
 *
 * @author galen
 * @date 2024/1/3115:33
 */
public interface ComponentConvertFactory<R, P, T extends ComponentConvert<R, P>> {

    default T getConvert(ComponentType type) {
        switch (type) {
            case TEXT:
                return text();
            case TEXTAREA:
                return textarea();
            case SELECT:
                return select();
            case MULTI_SELECT:
                return multiSelect();
            case DATE_RANGE:
                return dateRange();
            case ATTACHMENT:
                return attachment();
            case NUMBER:
                return number();
            case DATE:
                return date();
            case IMAGE:
                return image();
            case TABLE:
                return table();
            case AMOUNT:
                return amount();
            case UNKNOWN:
                return unknown();
            default:
                throw new WfException("未实现的组件类型:" + type);
        }
    }

    abstract T text();

    abstract T textarea();

    abstract T select();

    abstract T multiSelect();

    abstract T date();

    abstract T dateRange();

    abstract T number();

    abstract T amount();

    abstract T image();

    abstract T attachment();

    abstract T unknown();

    abstract T table();

}
