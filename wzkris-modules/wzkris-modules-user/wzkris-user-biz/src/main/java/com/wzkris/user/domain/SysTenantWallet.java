package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.core.constant.CommonConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 租户钱包表 sys_tenant_wallet
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class SysTenantWallet {

    @TableId
    private Long tenantId;

    @Schema(description = "余额, 元")
    private BigDecimal balance;

    @Schema(description = "状态")
    private String status;

    public SysTenantWallet(Long tenantId) {
        this.tenantId = tenantId;
        this.balance = BigDecimal.ZERO;
        this.status = CommonConstants.STATUS_ENABLE;
    }
}
