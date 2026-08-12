package com.wzkris.payment.api.customerbalance.response;

import com.wzkris.payment.enums.customerbalance.CustomerBalanceStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 客户余额信息展示
 */
@Data
public class CustomerBalanceInfoQueryResponse {

    @Schema(description = "余额, 元")
    private BigDecimal balance;

    @Schema(description = "状态")
    private CustomerBalanceStatusEnum status;

}