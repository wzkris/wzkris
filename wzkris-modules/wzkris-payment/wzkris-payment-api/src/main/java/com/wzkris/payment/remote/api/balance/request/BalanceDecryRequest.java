package com.wzkris.payment.remote.api.balance.request;

import com.wzkris.common.core.enums.AuthTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "余额扣减参数体")
public class BalanceDecryRequest {

    @NotNull(message = "认证类型不能为空")
    @Schema(description = "认证类型（仅租户/客户）")
    private AuthTypeEnum authType;

    @NotNull(message = "归属ID不能为空")
    @Schema(description = "归属ID（租户ID/客户ID）")
    private Long ownerId;

    @NotNull(message = "扣减金额不能为空")
    @DecimalMin(value = "0.01", message = "扣减金额必须大于0")
    @Schema(description = "扣减金额（元）")
    private BigDecimal amount;

    @NotBlank(message = "业务编号不能为空")
    @Schema(description = "业务编号（幂等键）")
    private String bizNo;

    @Schema(description = "业务类型")
    private String bizType;

    @Schema(description = "流水备注")
    private String remark;

}