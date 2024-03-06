package org.bigant.wf.process.bean;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import org.bigant.wf.process.form.FormDetailItem;

import java.util.List;

/**
 * 审批流程详情
 *
 * @author galen
 * @date 2024/2/1909:28
 */
@ApiModel("审批流程详情")
@Data
@Builder
public class ProcessDetail {

    private String processCode;

    private String name;

    private String iconUrl;

    private List<FormDetailItem> form;


}
