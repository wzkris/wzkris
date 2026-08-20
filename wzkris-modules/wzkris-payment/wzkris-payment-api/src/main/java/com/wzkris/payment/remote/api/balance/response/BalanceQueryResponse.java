package com.wzkris.payment.remote.api.balance.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "余额查询响应")
public class BalanceQueryResponse {

    @Schema(description = "归属ID")
    private Long ownerId;

    @Schema(description = "余额, 元")
    private BigDecimal balance;

}