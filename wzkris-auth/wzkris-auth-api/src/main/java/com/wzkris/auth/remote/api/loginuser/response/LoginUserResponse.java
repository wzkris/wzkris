package com.wzkris.auth.remote.api.loginuser.response;

import com.wzkris.common.core.model.BaseLoginUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
public class LoginUserResponse implements Serializable {

    private BaseLoginUser loginUser;

    private Set<String> permissions;

    public LoginUserResponse(BaseLoginUser loginUser, Set<String> permissions) {
        this.loginUser = loginUser;
        this.permissions = permissions;
    }

}


