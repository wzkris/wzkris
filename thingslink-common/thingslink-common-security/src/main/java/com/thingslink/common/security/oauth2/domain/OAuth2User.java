package com.thingslink.common.security.oauth2.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

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
@Data
@NoArgsConstructor
public class OAuth2User implements org.springframework.security.oauth2.core.user.OAuth2User, Serializable {
    @Serial
    private static final long serialVersionUID = 4803666659523397454L;

    private String oauth2Type;

    private String principalName;// 一般为用户名或客户端id

    private Object details;// 详细属性

    public OAuth2User(String oauth2Type, String principalName, Object details, Collection<? extends GrantedAuthority> authorities) {
        this.oauth2Type = oauth2Type;
        this.principalName = principalName;
        this.details = details;
        this.authorities = authorities;
    }

    private Collection<? extends GrantedAuthority> authorities;

    public Map<String, Object> getAttributes() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return this.principalName;
    }
}