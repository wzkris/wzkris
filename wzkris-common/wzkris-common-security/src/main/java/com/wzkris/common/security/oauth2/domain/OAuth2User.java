package com.wzkris.common.security.oauth2.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : OAuth2用户信息
 * @date : 2024/5/16 09:59
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class OAuth2User implements org.springframework.security.oauth2.core.user.OAuth2User, Serializable {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final String oauth2Type;

    private final String principalName;// 一般为用户名或客户端id

    private final Object principal;// 登录信息

    private final Collection<SimpleGrantedAuthority> authorities;// 权限

    @JsonCreator
    public OAuth2User(@JsonProperty("oauth2Type") String oauth2Type,
                      @JsonProperty("principalName") String principalName,
                      @JsonProperty("principal") Object principal,
                      @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities) {
        this.oauth2Type = oauth2Type;
        this.principalName = principalName;
        this.principal = principal;
        this.authorities = (Collection<SimpleGrantedAuthority>) authorities;
    }

    public Map<String, Object> getAttributes() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public String getName() {
        return this.principalName;
    }
}
