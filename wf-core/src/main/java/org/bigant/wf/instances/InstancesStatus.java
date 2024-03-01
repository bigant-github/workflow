package org.bigant.wf.instances;

/**
 * @author galen
 * @date 2024/2/2917:27
 */
public enum InstancesStatus {
    WAITING("未启动"),
    RUNNING("审批中"),
    AGREED("同意"),
    REFUSED("拒绝"),
    CANCELED("撤销"),
    DELETED("删除");
    private final String actionName;

    InstancesStatus(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }
}
