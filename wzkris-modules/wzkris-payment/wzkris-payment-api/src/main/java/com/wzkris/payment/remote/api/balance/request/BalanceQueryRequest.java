package com.wzkris.payment.remote.api.balance.request;

import com.wzkris.common.core.enums.AuthTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "余额查询参数体")
public class BalanceQueryRequest {

    @NotNull(message = "认证类型不能为空")
    @Schema(description = "认证类型（仅租户/客户）")
    private AuthTypeEnum authType;

    @NotNull(message = "归属ID不能为空")
    @Schema(description = "归属ID（租户ID/客户ID）")
    private Long ownerId;

}