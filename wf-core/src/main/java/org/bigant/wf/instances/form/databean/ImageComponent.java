package org.bigant.wf.instances.form.databean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 附件
 *
 * @author galen
 * @date 2024/2/111:56
 */
@Data
@ApiModel(value = "附件")
@Builder
public class ImageComponent {

    @ApiModelProperty("文件名")
    private String name;
    @ApiModelProperty("文件大小")
    private Long size;
    @ApiModelProperty("文件路径")
    private String url;

}
