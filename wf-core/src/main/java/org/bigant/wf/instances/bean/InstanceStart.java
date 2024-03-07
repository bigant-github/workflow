package org.bigant.wf.instances.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bigant.wf.instances.form.FormDataItem;

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
@AllArgsConstructor
@NoArgsConstructor
public class InstanceStart {
    @ApiModelProperty("审批流程code")
    private String processCode;

    @ApiModelProperty("发起人id")
    private String userId;

    @ApiModelProperty("发起人id")
    private String deptId;

    @ApiModelProperty("自选审批节点用户（与 authMatchSelectApproverUsers 只能二选一）")
    private List<NodeUser> selectApproverUsers;

    @ApiModelProperty("自选审批节点用户自动匹配 （与 selectApproverUsers 只能二选一）")
    private List<AuthMatchNodeUser> authMatchSelectApproverUsers;

    @ApiModelProperty("自选抄送节点用户 （与 autoMathSelectCcUsers 只能二选一）")
    private List<NodeUser> selectCcUsers;

    @ApiModelProperty("自选抄送节点用户自动匹配 （与 selectCcUsers 只能二选一）")
    private List<AuthMatchNodeUser> autoMathSelectCcUsers;

    @ApiModelProperty("表单字段集合")
    private List<FormDataItem> formData;




    @ApiModel("自选节点用户")
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NodeUser {
        @ApiModelProperty("节点key")
        private String key;
        @ApiModelProperty("用户id")
        private List<String> userIds;
    }

    @ApiModel("自选节点用户自动匹配")
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthMatchNodeUser {

        @ApiModelProperty("用户id")
        private List<String> userIds;
    }


}
