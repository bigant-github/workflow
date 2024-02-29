package org.bigant.wf.instances;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 实例事件
 *
 * @author galen
 * @date 2024/2/2811:29
 */
public interface InstancesAction {

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
