package org.bigant.wf.process.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.bigant.wf.ComponentType;

import java.util.List;

@Data
@ApiModel("表单字段")
@Builder
public class FormDetailItem {
    @ApiModelProperty("字段id")
    private String id;
    @ApiModelProperty("字段名称")
    private String name;
    @ApiModelProperty("字段类型")
    private ComponentType type;

    /**
     * 该字段对应 org.bigant.wf.form.option.* 这个包的实体，可与匹配的类型强转
     */
    @ApiModelProperty("附属信息")
    private Object option;
    @ApiModelProperty("子集")
    private List<FormDetailItem> children;

}
