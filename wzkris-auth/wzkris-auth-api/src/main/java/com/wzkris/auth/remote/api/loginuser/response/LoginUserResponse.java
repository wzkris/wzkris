package com.wzkris.auth.remote.api.loginuser.response;

import com.wzkris.common.core.model.LoginUser;
import com.wzkris.common.core.model.RoleContext;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class LoginUserResponse implements Serializable {

    private LoginUser loginUser;

    private RoleContext roleContext;

    public LoginUserResponse(LoginUser loginUser, RoleContext roleContext) {
        this.loginUser = loginUser;
        this.roleContext = roleContext;
    }

}
