package com.wzkris.auth.security.core;

import com.wzkris.common.core.model.CorePrincipal;
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

    private final CorePrincipal principal;

    protected CommonAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        this(authorities, null);
    }

    protected CommonAuthenticationToken(Collection<? extends GrantedAuthority> authorities, CorePrincipal principal) {
        super(authorities);
        this.principal = principal;
        if (principal != null) {
            super.setAuthenticated(true);
        }
    }

    public abstract String getLoginType();

    @Override
    public final CorePrincipal getPrincipal() {
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
