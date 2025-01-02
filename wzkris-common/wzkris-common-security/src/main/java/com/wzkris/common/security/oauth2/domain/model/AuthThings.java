package com.wzkris.common.security.oauth2.domain.model;

import com.wzkris.common.security.oauth2.domain.AuthBaseUser;
import com.wzkris.common.security.oauth2.enums.LoginType;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 授权事物
 * @date : 2024/5/16 15:36
 */
@Setter
@Getter
public class AuthThings extends AuthBaseUser {
    /**
     * 名称
     */
    private String principalName;

    public AuthThings(String principalName) {
        this(principalName, Collections.emptySet());
    }

    public AuthThings(String principalName, Set<String> grantedAuthority) {
        super(LoginType.AUTH_THINGS, grantedAuthority);
        this.principalName = principalName;
    }

    @Override
    public String getName() {
        return this.getPrincipalName();
    }

}
