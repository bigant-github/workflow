package org.bigant.wf.process.bean;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 审批流程详情
 *
 * @author galen
 * @date 2024/2/1909:28
 */
@ApiModel("审批流程详情")
@Data
public class ProcessDetail {

    private String code;
    private String name;
    private String iconUrl;


    private String form;




}
