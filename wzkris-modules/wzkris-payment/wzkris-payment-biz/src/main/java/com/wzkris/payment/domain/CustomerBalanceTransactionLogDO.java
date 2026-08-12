package com.wzkris.payment.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.payment.enums.balance.BalanceRecordTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 用户资金流水表
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(schema = "biz", value = "customer_balance_transaction_log")
public class CustomerBalanceTransactionLogDO extends BaseEntity {

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "金额, 元")
    private BigDecimal amount;

    @Schema(description = "记录类型")
    private BalanceRecordTypeEnum recordType;

    @Schema(description = "业务编号")
    private String bizNo;

    @Schema(description = "业务类型")
    private String bizType;

    @Schema(description = "变动前余额")
    private BigDecimal beforeBalance;

    @Schema(description = "变动后余额")
    private BigDecimal afterBalance;

    @Schema(description = "备注")
    private String remark;

}