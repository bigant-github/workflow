package org.bigant.wf.instances.form.databean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bigant.wf.process.form.option.AmountOption;

import java.math.BigDecimal;

/**
 * 附件
 *
 * @author galen
 * @date 2024/2/111:56
 */
@Data
@ApiModel(value = "附件")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormDataAmount {

    @ApiModelProperty("金额")
    private BigDecimal amount;
    @ApiModelProperty("币种")
    private AmountOption.AmountType amountType;

}
