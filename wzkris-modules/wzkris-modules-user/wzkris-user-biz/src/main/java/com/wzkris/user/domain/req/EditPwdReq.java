package com.wzkris.user.domain.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import lombok.Data;

/**
 * 修改密码
 */
@Data
public class EditPwdReq {

    @NotBlank(message = "{desc.oldpwd}" + "{validate.notnull}", groups = {LoginPwd.class, OperPwd.class})
    private String oldPassword;

    @Pattern(regexp = "^\\d{6}$", message = "{desc.newpwd}{validate.illegal}", groups = OperPwd.class)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])\\S{8,20}$", message = "{desc.newpwd}{validate.illegal}", groups = LoginPwd.class)
    @NotBlank(message = "{desc.newpwd}" + "{validate.notnull}", groups = {LoginPwd.class, OperPwd.class})
    private String newPassword;

    // 登录密码校验
    public interface LoginPwd extends Default {

    }

    // 操作密码校验
    public interface OperPwd extends Default {

    }
}
