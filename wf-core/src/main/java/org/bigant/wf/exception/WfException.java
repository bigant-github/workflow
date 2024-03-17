package org.bigant.wf.exception;

/**
 * 审批流异常
 *
 * @author galen
 * date 2024/2/515:09
 */
public class WfException extends RuntimeException {

    public WfException(String message, Throwable cause) {
        super(message, cause);
    }

    public WfException(String message) {
        super(message);
    }
}
