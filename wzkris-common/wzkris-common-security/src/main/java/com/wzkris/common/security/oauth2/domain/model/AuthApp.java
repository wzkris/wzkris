package com.wzkris.common.security.oauth2.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    public AuthApp(String id, String principalName) {
        this(id, principalName, Collections.emptySet());
    }

    @JsonCreator
    public AuthApp(@JsonProperty("id") String id, @JsonProperty("principalName") String principalName,
                   @JsonProperty("grantedAuthority") Set<String> grantedAuthority) {
        super(id, LoginType.AUTH_APP, grantedAuthority);
        this.principalName = principalName;
    }

    @Override
    public String getName() {
        return this.getPrincipalName();
    }

}
