package org.bigant.wf.form.component.bean;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 时间区间
 *
 * @author galen
 * @date 2024/2/111:56
 */
@Data
@ApiModel(value = "时间区间")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DateRange {

    @ApiModelProperty("开始时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime begin;

    @ApiModelProperty("结束时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end;
}
