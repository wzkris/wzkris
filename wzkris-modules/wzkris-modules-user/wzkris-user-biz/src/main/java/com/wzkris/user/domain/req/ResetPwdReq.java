package com.wzkris.user.domain.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 重置密码
 */
@Data
public class ResetPwdReq {
    @NotNull(message = "[userId] {validate.notnull}")
    private Long userId;

    @NotBlank(message = "[password] {validate.notnull}")
    private String password;
}
