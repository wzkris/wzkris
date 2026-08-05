package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.usercenter.enums.tenantwallet.TenantWalletWithdrawalStatusEnum;
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
@TableName(schema = "biz", value = "tenant_wallet_withdrawal_record")
public class TenantWalletWithdrawalRecordDO extends BaseEntity {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "状态")
    private TenantWalletWithdrawalStatusEnum status;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "第三方请求参数")
    private String requestParams;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "完成时间")
    private OffsetDateTime completeAt;

    @Schema(description = "备注")
    private String remark;

}
