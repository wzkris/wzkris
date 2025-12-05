package com.wzkris.auth.security.core;

import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.common.core.model.MyPrincipal;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description AuthenticationToken基类，适配多端登录参数
 */
public abstract class CommonAuthenticationToken extends AbstractAuthenticationToken {

    private final MyPrincipal principal;

    protected CommonAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        this(authorities, null);
    }

    protected CommonAuthenticationToken(Collection<? extends GrantedAuthority> authorities, MyPrincipal principal) {
        super(authorities);
        this.principal = principal;
        if (principal != null) {
            super.setAuthenticated(true);
        }
    }

    public abstract LoginTypeEnum getLoginType();

    @Override
    public final MyPrincipal getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        throw new UnsupportedOperationException("Cannot set authenticated to " + this.getClass().getSimpleName());
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        if (principal != null) {
            return AuthorityUtils.createAuthorityList(principal.getPermissions());
        }
        return super.getAuthorities();
    }

}
