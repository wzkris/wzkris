package com.wzkris.payment.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseTenantEntity;
import com.wzkris.payment.enums.tenantbalance.TenantBalanceWithdrawalStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * 系统提现记录
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(schema = "biz", value = "tenant_balance_withdrawal_log")
public class TenantBalanceWithdrawalLogDO extends BaseTenantEntity {

    @Schema(description = "提现单号")
    private String withdrawalNo;

    @Schema(description = "打款渠道")
    private String channel;

    @Schema(description = "渠道配置id")
    private Long configId;

    @Schema(description = "提现金额, 元")
    private BigDecimal withdrawalAmount;

    @Schema(description = "币种")
    private String currency;

    @Schema(description = "状态")
    private TenantBalanceWithdrawalStatusEnum status;

    @Schema(description = "失败原因")
    private String failReason;

    @Schema(description = "渠道提现单号")
    private String channelWithdrawalNo;

    @Schema(description = "提现完成时间")
    private OffsetDateTime withdrawalAt;

}