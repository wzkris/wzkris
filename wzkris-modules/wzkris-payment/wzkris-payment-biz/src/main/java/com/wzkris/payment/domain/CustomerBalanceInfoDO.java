package com.wzkris.payment.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.payment.enums.customerbalance.CustomerBalanceStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 用户账户表
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(schema = "biz", value = "customer_balance_info")
public class CustomerBalanceInfoDO extends BaseEntity {

    @Schema(description = "客户ID")
    private Long customerId;

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
    private CustomerBalanceStatusEnum status;

    public CustomerBalanceInfoDO(Long customerId) {
        this.customerId = customerId;
        this.currency = "CNY";
        this.balance = BigDecimal.ZERO;
        this.frozen = BigDecimal.ZERO;
        this.totalIn = BigDecimal.ZERO;
        this.totalOut = BigDecimal.ZERO;
        this.status = CustomerBalanceStatusEnum.ENABLE;
    }

}