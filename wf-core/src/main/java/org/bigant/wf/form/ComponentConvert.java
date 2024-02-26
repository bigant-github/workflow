package org.bigant.wf.form;

import org.bigant.wf.exception.WfException;
import org.bigant.wf.form.component.ComponentType;

/**
 * 组件转换器
 *
 * @author galen
 * @date 2024/2/2115:35
 */
public abstract class ComponentConvert<T, R> {

    public R convert(ComponentType type, T component) {
        switch (type) {
            case TEXT:
                return text(component);
            case TEXTAREA:
                return textarea(component);
            case SELECT:
                return select(component);
            case MULTI_SELECT:
                return multiSelect(component);
            case DATE_RANGE:
                return dateRange(component);
            case ATTACHMENT:
                return attachment(component);
            case NUMBER:
                return number(component);
            case DATE:
                return date(component);
            case IMAGE:
                return image(component);
            case TABLE:
                return table(component);
            case AMOUNT:
                return amount(component);
            case UNKNOWN:
                return unknown(component);
            default:
                throw new WfException("未实现的组件类型:" + type);
        }
    }
    protected abstract R text(T component);
    protected abstract R textarea(T component);
    protected abstract R select(T component);
    protected abstract R multiSelect(T component);
    protected abstract R date(T component);
    protected abstract R dateRange(T component);
    protected abstract R number(T component);
    protected abstract R amount(T component);
    protected abstract R image(T component);
    protected abstract R attachment(T component);
    protected abstract R unknown(T component);
    protected abstract R table(T component);










}
