package com.wzkris.gateway.remote.interfaces.loginuser.response;

import com.wzkris.common.core.model.DefaultLoginUser;
import com.wzkris.common.core.model.RoleContext;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class LoginUserResponse implements Serializable {

    private DefaultLoginUser loginUser;

    private RoleContext roleContext;

}
