package org.bigant.wf;

/**
 * 所有组件方法
 *
 * @author galen
 * @date 2024/1/3115:33
 */
public interface ComponentTypeGet<T> {

    T text();

    T textarea();

    T select();

    T multiSelect();

    T date();

    T dateRange();

    T number();

    T amount();

    T image();

    T attachment();

    T table();

    T joinInstance();

    T unknown();

}
