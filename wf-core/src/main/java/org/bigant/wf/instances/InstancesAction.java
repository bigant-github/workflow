package org.bigant.wf.instances;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bigant.wf.exception.WfException;

import java.time.LocalDateTime;

/**
 * 实例事件
 *
 * @author galen
 * @date 2024/2/2811:29
 */
public interface InstancesAction {

    default public void action(InstancesCallback callback) {
        switch (callback.getAction()) {
            case START:
                start(callback);
                break;
            case AGREE:
                agree(callback);
                break;
            case REFUSE:
                refuse(callback);
                break;
            case CANCEL:
                cancel(callback);
                break;
            case CLOSE:
                close(callback);
                break;
            default:
                throw new WfException("无法识别的审批实例：" + callback.getAction());
        }
    }

    void start(InstancesCallback callback);

    void agree(InstancesCallback callback);

    void refuse(InstancesCallback callback);

    void cancel(InstancesCallback callback);

    void close(InstancesCallback callback);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class InstancesCallback {

        private String processCode;

        private String instanceCode;

        private LocalDateTime operateTime;

        private InstancesActionEnum action;
    }


    public static enum InstancesActionEnum {
        START("start"),
        AGREE("agree"),
        REFUSE("refuse"),
        CANCEL("cancel"),
        CLOSE("close");
        private final String actionName;

        InstancesActionEnum(String actionName) {
            this.actionName = actionName;
        }

        public String getActionName() {
            return actionName;
        }
    }

}
