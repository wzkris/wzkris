package com.wzkris.auth.security.core;

import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.common.core.model.BaseLoginUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.Set;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description AuthenticationToken基类，适配多端登录参数
 */
public class CommonAuthenticationToken extends AbstractAuthenticationToken {

    private final BaseLoginUser baseLoginUser;

    @Getter
    private final Set<String> perms;

    @Getter
    private final LoginTypeEnum loginType;

    @Getter
    @Setter
    private String accessToken;

    @Getter
    @Setter
    private String refreshToken;

    public CommonAuthenticationToken(BaseLoginUser baseLoginUser, Set<String> perms, LoginTypeEnum loginType) {
        super(null);
        this.baseLoginUser = baseLoginUser;
        this.perms = perms;
        this.loginType = loginType;
        if (baseLoginUser != null) {
            super.setAuthenticated(true);
        }
    }

    @Override
    public Object getCredentials() {
        return accessToken;
    }

    @Override
    public final BaseLoginUser getPrincipal() {
        return this.baseLoginUser;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        throw new UnsupportedOperationException("Cannot set authenticated to " + this.getClass().getSimpleName());
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        if (perms != null) {
            return AuthorityUtils.createAuthorityList(perms);
        }
        return super.getAuthorities();
    }

}
