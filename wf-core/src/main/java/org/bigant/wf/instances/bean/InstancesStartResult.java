package org.bigant.wf.instances.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 发起审批实例结果
 *
 * @author galen
 * @date 2024/1/3114:54
 */
@ApiModel("发起审批实例结果")
@Data
@ToString
@Builder
public class InstancesStartResult {


    @ApiModelProperty("流程code")
    private String processCode;

    @ApiModelProperty("实例code")
    private String instanceCode;
}
