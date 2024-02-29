package org.bigant.wf.instances;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 实例事件
 *
 * @author galen
 * @date 2024/2/2811:29
 */
public interface InstancesAction {

    void start(InstancesCallback callback);

    void approved(InstancesCallback callback);

    void rejected(InstancesCallback callback);

    void canceled(InstancesCallback callback);

    void close(InstancesCallback callback);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class InstancesCallback{
        private String instanceCode;

        private LocalDateTime operateTime;
    }

}
