package org.bigant.wf.form.component;

/**
 * 组建工具
 *
 * @author galen
 * @date 2024/1/3116:30
 */
public interface ComponentParse<T> {

    T toJava(String str);

    String toStr(T data);

    void verify(T data);

    ComponentType type();

    public static class VerifyException extends RuntimeException {

        public VerifyException(String message) {
            super(message);
        }
    }
}
