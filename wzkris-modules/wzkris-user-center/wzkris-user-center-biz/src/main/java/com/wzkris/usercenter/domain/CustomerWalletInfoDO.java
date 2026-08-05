package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.usercenter.enums.customerwallet.CustomerWalletStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 用户钱包表
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(schema = "biz", value = "customer_wallet_info")
public class CustomerWalletInfoDO extends BaseEntity {

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "余额, 元")
    private BigDecimal balance;

    @Schema(description = "状态")
    private CustomerWalletStatusEnum status;

    public CustomerWalletInfoDO(Long customerId) {
        this.customerId = customerId;
        this.balance = BigDecimal.ZERO;
        this.status = CustomerWalletStatusEnum.ENABLE;
    }

}
