package com.wzkris.user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户登录对象
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LoginPwdDTO {
    @NotBlank(message = "[username] {validate.notnull}")
    private String username;
    @NotBlank(message = "[password] {validate.notnull}")
    private String password;
    // 验证码id
    private String uuid;
    // 验证码
    private String code;
}
