package org.bigant.wf.instances.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.bigant.wf.instances.form.FormDataItem;
import org.bigant.wf.instances.InstanceStatus;
import org.bigant.wf.task.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 发起审批实例实体
 *
 * @author galen
 * @date 2024/1/3114:54
 */
@Data
@ApiModel("发起审批实例")
@Builder
public class InstanceDetailResult {
    @ApiModelProperty("审批实例code")
    private String instanceCode;

    @ApiModelProperty("审批流程code")
    private String processCode;

    @ApiModelProperty("发起人id")
    private String userId;

    @ApiModelProperty("发起人id")
    private String deptId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("审批状态")
    private InstanceStatus instanceStatus;

    @ApiModelProperty("表单字段集合")
    private List<FormDataItem> formData;

    @ApiModelProperty("表单字段集合")
    private List<Task> tasks;


    @Data
    @ApiModel("任务")
    @Builder
    public static class Task {
        @ApiModelProperty("任务id")
        private String taskCode;

        @ApiModelProperty("用户id")
        private String userId;

        @ApiModelProperty("用户名称")
        private String userName;

        @ApiModelProperty("任务状态")
        private TaskStatus taskStatus;

        @ApiModelProperty("任务结束时间")
        private LocalDateTime endTime;
    }

}
