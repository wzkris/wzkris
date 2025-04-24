package com.wzkris.common.security.oauth2.domain.model;

import com.wzkris.common.security.oauth2.domain.AuthBaseUser;
import com.wzkris.common.security.oauth2.enums.LoginType;
import lombok.Getter;

import java.util.Collections;
import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 授权APP
 * @date : 2024/5/16 15:36
 */
@Getter
public class AuthApp extends AuthBaseUser {

    /**
     * app名称
     */
    private final String principalName;

    public AuthApp(String principalName) {
        this(principalName, Collections.emptySet());
    }

    public AuthApp(String principalName, Set<String> grantedAuthority) {
        super(LoginType.AUTH_APP, grantedAuthority);
        this.principalName = principalName;
    }

    @Override
    public String getName() {
        return this.getPrincipalName();
    }

}
