package com.wzkris.gateway.remote.loginuser.resp;

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

}
