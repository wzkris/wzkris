package com.wzkris.gateway.remote.api.loginuser.response;

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

}
