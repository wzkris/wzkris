package com.wzkris.usercenter.api.tenantwallet.response;

import com.wzkris.usercenter.enums.tenantwallet.TenantWalletStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 租户钱包信息展示
 */
@Data
public class TenantWalletInfoQueryResponse {

    @Schema(description = "余额, 元")
    private BigDecimal balance;

    @Schema(description = "状态")
    private TenantWalletStatusEnum status;

}

