package com.wzkris.payment.api.tenantbalance.response;

import com.wzkris.payment.enums.tenantbalance.TenantBalanceStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 租户余额信息展示
 */
@Data
public class TenantBalanceInfoQueryResponse {

    @Schema(description = "余额, 元")
    private BigDecimal balance;

    @Schema(description = "状态")
    private TenantBalanceStatusEnum status;

}