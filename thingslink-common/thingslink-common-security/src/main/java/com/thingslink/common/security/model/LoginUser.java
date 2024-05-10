package com.thingslink.common.security.model;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 登录用户信息抽象类
 * @date : 2024/4/9 09:29
 */
@Setter
public abstract class LoginUser implements UserDetails, OAuth2User, CredentialsContainer, Serializable {

    @Serial
    private static final long serialVersionUID = 2978369205157234626L;

    // 权限
    private Collection<? extends GrantedAuthority> authorities;

    // 额外参数
    @Getter
    private Map<String, Object> additionalParameters;

    public abstract Long getUserId();

    // 用于判断用户类型
    public abstract String getUserType();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities == null ? AuthorityUtils.NO_AUTHORITIES : authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.EMPTY_MAP;
    }

    public void putAdditionalParameter(String key, Object value) {
        if (this.additionalParameters == null) {
            this.additionalParameters = new HashMap<>(4);
        }
        this.additionalParameters.put(key, value);
    }

    @Override
    public abstract String getPassword();

    @Override
    @Nonnull
    public abstract String getUsername();

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return this.getUsername();
    }

    @Override
    public void eraseCredentials() {
    }
}
