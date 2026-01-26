package com.wzkris.auth.security.core;

import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.service.TokenService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description provider基类，验证Authentication
 */
public abstract class CommonAuthenticationProvider implements AuthenticationProvider {

    private final TokenService tokenService;

    protected CommonAuthenticationProvider(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * 认证核心方法
     */
    protected abstract CommonAuthenticationToken doAuthenticate(Authentication authentication);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CommonAuthenticationToken authenticationToken = this.doAuthenticate(authentication);

        return this.buildAuthenticationToken(authenticationToken);
    }

    final CommonAuthenticationToken buildAuthenticationToken(CommonAuthenticationToken authenticationToken) {
        String generatedToken = tokenService.generateAccessToken(authenticationToken.getPrincipal());

        String refreshToken;
        if (authenticationToken.getLoginType() == LoginTypeEnum.REFRESH) {
            refreshToken = authenticationToken.getRefreshToken();
        } else {
            refreshToken = tokenService.generateToken();
        }
        authenticationToken.setAccessToken(generatedToken);
        authenticationToken.setRefreshToken(refreshToken);

        // 保存用户信息和权限信息
        tokenService.save(authenticationToken.getPrincipal(), generatedToken, refreshToken, authenticationToken.getPerms());

        return authenticationToken;
    }

}
