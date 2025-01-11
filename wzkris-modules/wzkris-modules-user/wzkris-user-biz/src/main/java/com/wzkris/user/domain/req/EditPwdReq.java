package com.wzkris.user.domain.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 修改密码
 */
@Data
public class EditPwdReq {

    @NotBlank(message = "{desc.oldpwd}" + "{validate.notnull}")
    private String oldPassword;

    @NotBlank(message = "{desc.newpwd}" + "{validate.notnull}")
    private String newPassword;

}
