package org.bigant.wf.task;

/**
 * 任务状态
 *
 * @author galen
 * @date 2024/3/109:21
 */
public enum TaskStatus {

    WAITING("未启动"),
    RUNNING("处理中"),
    PAUSED("暂停"),
    CANCELED("取消"),
    AGREED("同意"),
    REFUSED("拒绝"),
    REDIRECTED("转交");
    final String statusName;

    TaskStatus(String statusName) {
        this.statusName = statusName;
    }

}
