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

    @NotBlank(message = "{desc.pwd}" + "{validate.notnull}")
    @Size(min = 6, max = 6, message = "{desc.pwd}" + "{validate.size.illegal}")
    private String operPwd;

}
