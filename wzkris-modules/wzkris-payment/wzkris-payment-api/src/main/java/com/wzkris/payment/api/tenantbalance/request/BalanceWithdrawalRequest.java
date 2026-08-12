package com.wzkris.payment.api.tenantbalance.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 提现请求体
 *
 * @author wzkris
 */
@Data
public class BalanceWithdrawalRequest {

    @NotNull(message = "提现金额不能为空")
    @DecimalMin(value = "1.00", message = "提现金额最少为1元")
    private BigDecimal amount;

    @NotBlank(message = "支付密码不能为空")
    @Size(min = 6, max = 20, message = "支付密码长度为6-20位")
    private String payPwd;

}