package org.bigant.wf.form.component.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class AmountComponent {

    @ApiModelProperty("金额")
    private BigDecimal amount;
    @ApiModelProperty("币种")
    private AmountType amountType;


    public static enum AmountType {
        CNY("人民币"),
        USD("美元"),
        EUR("欧元"),
        GBP("英镑"),
        HKD("港币"),
        JPY("日元"),
        TWD("新台币"),
        AUD("澳元"),
        NZD("新西兰元"),
        CHF("瑞士法郎"),
        SEK("瑞典克朗"),
        NOK("挪威克朗"),
        CAD("加拿大元");

        private String desc;

        AmountType(String desc) {
            this.desc = desc;
        }


    }

}
