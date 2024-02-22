package org.bigant.wf.instances.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 发起审批实例实体
 *
 * @author galen
 * @date 2024/1/3114:54
 */
@Data
@ApiModel("发起审批实例")
public class InstancesPreviewResult {

    @ApiModelProperty("审批实例code")
    private String code;

    @ApiModelProperty("发起人id")
    private String userId;

    @ApiModelProperty("发起人id")
    private String deptId;


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
    public static class TargetSelectUserAuthMatch {

        @ApiModelProperty("用户id")
        private List<String> userIds;
    }


}
