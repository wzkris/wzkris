package com.wzkris.user.domain.req;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 提现请求体
 *
 * @author wzkris
 */
@Data
public class WithdrawalReq {

    @DecimalMin(value = "1.00", message = "提现金额最少为1元")
    private BigDecimal amount;

    @NotBlank(message = "请输入操作密码")
    @Size(min = 6, max = 6, message = "操作密码错误")
    private String operPwd;
}
