package org.bigant.wf.instances.form;

import org.bigant.wf.ComponentType;

/**
 * 组建工具
 *
 * @author galen
 * date 2024/1/3116:30
 */
public interface FormDataParse<T> {

    T strToJava(String str);

    String toStr(T data);

    void verify(T data);

    ComponentType type();

    default T toJava(Object data) {
        return (T) data;
    }

    public static class VerifyException extends RuntimeException {

        public VerifyException(String message) {
            super(message);
        }
    }
}
