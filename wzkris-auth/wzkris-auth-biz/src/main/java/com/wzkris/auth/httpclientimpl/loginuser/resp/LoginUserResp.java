package com.wzkris.auth.httpclientimpl.loginuser.resp;

import com.wzkris.common.core.model.BaseLoginUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
public class LoginUserResp implements Serializable {

    private BaseLoginUser loginUser;

    private Set<String> permissions;

    public LoginUserResp(BaseLoginUser loginUser, Set<String> permissions) {
        this.loginUser = loginUser;
        this.permissions = permissions;
    }

}
