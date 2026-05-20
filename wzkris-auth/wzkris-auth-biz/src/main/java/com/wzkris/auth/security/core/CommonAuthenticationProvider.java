package com.wzkris.auth.security.core;

import com.wzkris.auth.domain.TokenPair;
import com.wzkris.auth.security.core.refresh.RefreshAuthenticationToken;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.model.BaseLoginUser;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;

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
        BaseLoginUser loginUser = (BaseLoginUser) authenticated.getPrincipal();
        var perms = AuthorityUtils.authorityListToSet(authenticated.getAuthorities());
        TokenPair tokenPair = authentication instanceof RefreshAuthenticationToken refresh
                ? tokenService.refresh(loginUser, perms, refresh.getRefreshToken())
                : tokenService.login(loginUser, perms);
        authenticated.setDetails(tokenPair);
        return authenticated;
    }

}
