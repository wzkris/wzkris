package com.wzkris.payment.api.tenantbalance.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 设置提现支付密码请求体
 *
 * @author wzkris
 */
@Data
public class SetPayPasswordRequest {

    @NotBlank(message = "支付密码不能为空")
    @Size(min = 6, max = 20, message = "支付密码长度为6-20位")
    private String payPwd;

}