package com.wzkris.user.domain.vo;

import com.wzkris.user.domain.SysTenant;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 租户信息展示层
 *
 * @author wzkris
 */
@Data
public class SysTenantVO extends SysTenant {

    @Schema(description = "套餐名称")
    private String packageName;

    @Schema(description = "余额, 元")
    private BigDecimal balance;
}
