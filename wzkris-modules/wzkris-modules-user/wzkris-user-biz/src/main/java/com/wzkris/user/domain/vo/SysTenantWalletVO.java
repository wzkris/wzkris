package com.wzkris.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SysTenantWalletVO {

    @Schema(description = "余额, 元")
    private BigDecimal balance;

    @Schema(description = "状态")
    private String status;
}
