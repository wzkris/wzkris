package com.wzkris.user.domain.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 重置密码
 */
@Data
public class ResetPwdReq {

    @NotNull(message = "{desc.id}{validate.notnull}")
    private Long id;

    @NotBlank(message = "{desc.pwd}{validate.notnull}")
    private String password;
}
