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
 * date 2024/1/3114:54
 */
@Data
@ApiModel("发起审批实例")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstanceCancel {
    @ApiModelProperty("审批流程code")
    private String processCode;

    @ApiModelProperty("实例code")
    private String instanceCode;

    @ApiModelProperty("发起人id")
    private String userId;

    @ApiModelProperty("发起人部门id")
    private String deptId;

    @ApiModelProperty("发起人备注")
    private String remark;

}
