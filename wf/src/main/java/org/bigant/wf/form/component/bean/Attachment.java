package org.bigant.wf.form.component.bean;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 附件
 *
 * @author galen
 * @date 2024/2/111:56
 */
@Data
@ApiModel(value = "附件")
public class Attachment {

    @ApiModelProperty("文件名")
    private String name;
    @ApiModelProperty("文件大小")
    private String size;
    @ApiModelProperty("文件路径")
    private String url;

}
