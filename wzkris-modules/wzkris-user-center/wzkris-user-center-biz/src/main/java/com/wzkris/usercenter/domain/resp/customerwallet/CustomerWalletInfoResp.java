package com.wzkris.usercenter.domain.resp.customerwallet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 客户钱包信息展示
 */
@Data
public class CustomerWalletInfoResp {

    @Schema(description = "余额, 元")
    private BigDecimal balance;

    @Schema(description = "状态")
    private String status;

}
