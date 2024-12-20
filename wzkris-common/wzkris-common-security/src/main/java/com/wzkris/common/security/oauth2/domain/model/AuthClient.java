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
 * @description : 授权客户端
 * @date : 2024/5/16 15:36
 */
@Setter
@Getter
public class AuthClient extends AuthBaseUser {
    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 客户端名称
     */
    private String clientName;

    public AuthClient() {
        this(Collections.emptySet());
    }

    public AuthClient(Set<String> grantedAuthority) {
        super(LoginType.AUTH_CLIENT, grantedAuthority);
    }

    @Override
    public String getName() {
        return this.getClientName();
    }

}
