package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseTenantEntity;
import com.wzkris.usercenter.enums.tenantwallet.TenantWalletStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 租户钱包表
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(schema = "biz", value = "tenant_wallet_info")
public class TenantWalletInfoDO extends BaseTenantEntity {

    @Schema(description = "余额, 元")
    private BigDecimal balance;

    @Schema(description = "状态")
    private TenantWalletStatusEnum status;

    public TenantWalletInfoDO(Long tenantId) {
        setTenantId(tenantId);
        this.balance = BigDecimal.ZERO;
        this.status = TenantWalletStatusEnum.ENABLE;
    }

}
