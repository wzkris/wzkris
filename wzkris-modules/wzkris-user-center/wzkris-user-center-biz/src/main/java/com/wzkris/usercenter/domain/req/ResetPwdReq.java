package com.wzkris.usercenter.domain.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 重置密码
 */
@Data
public class ResetPwdReq {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long id;

    @NotBlank(message = "{invalidParameter.password.invalid}")
    private String password;

}
