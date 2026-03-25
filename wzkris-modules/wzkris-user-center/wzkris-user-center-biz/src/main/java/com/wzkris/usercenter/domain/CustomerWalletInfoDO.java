package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.core.constant.CommonConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 用户钱包表
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "customer_wallet_info")
public class CustomerWalletInfoDO {

    @TableId
    private Long customerId;

    @Schema(description = "余额, 元")
    private BigDecimal balance;

    @Schema(description = "状态")
    private String status;

    public CustomerWalletInfoDO(Long customerId) {
        this.customerId = customerId;
        this.balance = BigDecimal.ZERO;
        this.status = CommonConstants.STATUS_ENABLE;
    }

}
