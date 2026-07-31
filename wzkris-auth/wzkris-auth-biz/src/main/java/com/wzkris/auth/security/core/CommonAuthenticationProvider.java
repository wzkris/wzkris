package com.wzkris.auth.security.core;

import com.wzkris.auth.domain.TokenPair;
import com.wzkris.auth.security.core.refresh.RefreshAuthenticationToken;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.model.RoleContext;
import com.wzkris.common.core.support.LoginUser;
import com.wzkris.common.security.authentication.RoleContextAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Provider 基类：子类完成校验后返回已认证的 {@link UsernamePasswordAuthenticationToken}，由本类统一签发 JWT。
 */
public abstract class CommonAuthenticationProvider implements AuthenticationProvider {

    private final TokenService tokenService;

    protected CommonAuthenticationProvider(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    protected abstract UsernamePasswordAuthenticationToken doAuthenticate(Authentication authentication);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticated = doAuthenticate(authentication);
        LoginUser loginUser = (LoginUser) authenticated.getPrincipal();
        RoleContext roleContext = authenticated instanceof RoleContextAuthenticationToken rcToken ? rcToken.getRoleContext() : null;
        TokenPair tokenPair = authentication instanceof RefreshAuthenticationToken refresh
                ? tokenService.loginRefresh(loginUser, roleContext, refresh.getRefreshToken())
                : tokenService.loginCreate(loginUser, roleContext);
        authenticated.setDetails(tokenPair);
        return authenticated;
    }

}
