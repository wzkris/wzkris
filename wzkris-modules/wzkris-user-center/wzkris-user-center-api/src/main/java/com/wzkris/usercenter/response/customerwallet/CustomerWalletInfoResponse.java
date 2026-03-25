package com.wzkris.usercenter.response.customerwallet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 客户钱包信息展示
 */
@Data
public class CustomerWalletInfoResponse {

    @Schema(description = "余额, 元")
    private BigDecimal balance;

    @Schema(description = "状态")
    private String status;

}

