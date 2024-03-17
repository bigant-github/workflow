package org.bigant.wf.process.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 表单列表
 *
 * @author galen
 * date 2024/1/3015:41
 */
@ApiModel("表单列表")
@Data
@Builder
public class ProcessPage {

    @ApiModelProperty("图标url")
    public String iconUrl;

    @ApiModelProperty("标题")
    public String title;

    @ApiModelProperty("表单编码")
    public String code;

    @ApiModelProperty("更新时间")
    public LocalDateTime updateTime;
}
