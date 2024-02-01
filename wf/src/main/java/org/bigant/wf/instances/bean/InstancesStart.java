package org.bigant.wf.instances.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.bigant.wf.form.bean.FormComponent;
import org.bigant.wf.form.component.ComponentType;
import org.bigant.wf.form.component.bean.Attachment;
import org.bigant.wf.form.component.bean.DateRange;

import java.time.LocalDateTime;
import java.util.Collection;
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
public class InstancesStart {
    @ApiModelProperty("审批实例code")
    private String code;

    @ApiModelProperty("发起人id")
    private String userId;

    @ApiModelProperty("发起人id")
    private String deptId;

    @ApiModelProperty("自选节点用户（与 targetSelectUsersAuthMatch 只能二选一）")
    private List<TargetSelectUser> targetSelectUsers;

    @ApiModelProperty("自选节点用户自动匹配 （与 targetSelectUsers 只能二选一）")
    private List<TargetSelectUserAuthMatch> targetSelectUsersAuthMatch;

    @ApiModelProperty("表单字段集合")
    private List<FormComponent> formComponents;


    @ApiModel("自选节点用户")
    @Data
    public static class TargetSelectUser {
        @ApiModelProperty("节点key")
        private String key;
        @ApiModelProperty("用户id")
        private List<String> userIds;
    }

    @Data
    @ApiModel("自选节点用户自动匹配")
    @Builder
    public static class TargetSelectUserAuthMatch {

        @ApiModelProperty("用户id")
        private List<String> userIds;
    }



}
