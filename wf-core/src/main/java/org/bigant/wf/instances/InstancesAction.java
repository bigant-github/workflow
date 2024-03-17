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
 * date 2024/2/2811:29
 */
public interface InstancesAction {

    default public void action(InstancesCallback callback) {
        switch (callback.getAction()) {
            case RUNNING:
                running(callback);
                break;
            case AGREED:
                agreed(callback);
                break;
            case REFUSED:
                refused(callback);
                break;
            case CANCELED:
                canceled(callback);
                break;
            case DELETED:
                deleted(callback);
                break;
            default:
                throw new WfException("无法识别的审批实例：" + callback.getAction());
        }
    }

    void running(InstancesCallback callback);

    void agreed(InstancesCallback callback);

    void refused(InstancesCallback callback);

    void canceled(InstancesCallback callback);

    void deleted(InstancesCallback callback);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class InstancesCallback {

        private String processCode;

        private String instanceCode;

        private String channelName;

        private LocalDateTime operateTime;

        private InstanceStatus action;
    }


}
