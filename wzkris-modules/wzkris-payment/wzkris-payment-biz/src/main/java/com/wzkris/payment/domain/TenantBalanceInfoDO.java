package com.wzkris.payment.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseTenantEntity;
import com.wzkris.payment.enums.tenantbalance.TenantBalanceStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 租户账户表
 *
 * <p>账户归属者数据（租户ID），非平台租户隔离 schema。
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(schema = "biz", value = "tenant_balance_info")
public class TenantBalanceInfoDO extends BaseTenantEntity {

    @Schema(description = "币种")
    private String currency;

    @Schema(description = "可用余额, 元")
    private BigDecimal balance;

    @Schema(description = "冻结余额, 元(预留)")
    private BigDecimal frozen;

    @Schema(description = "累计入账, 元")
    private BigDecimal totalIn;

    @Schema(description = "累计支出, 元")
    private BigDecimal totalOut;

    @Schema(description = "状态")
    private TenantBalanceStatusEnum status;

    @Schema(description = "提现支付密码（bcrypt）")
    private String payPassword;

    public TenantBalanceInfoDO(Long tenantId) {
        setTenantId(tenantId);
        this.currency = "CNY";
        this.balance = BigDecimal.ZERO;
        this.frozen = BigDecimal.ZERO;
        this.totalIn = BigDecimal.ZERO;
        this.totalOut = BigDecimal.ZERO;
        this.status = TenantBalanceStatusEnum.ENABLE;
    }

}