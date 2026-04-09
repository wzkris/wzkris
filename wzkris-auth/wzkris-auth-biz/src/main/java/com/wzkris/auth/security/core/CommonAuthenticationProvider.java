package com.wzkris.auth.security.core;

import com.wzkris.auth.domain.TokenPair;
import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.model.BaseLoginUser;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Provider基类，验证Authentication
 * <p>
 * 负责构建认证token，包括：
 * <ul>
 *   <li>生成sid（首次登录时生成新的，刷新token时保持原sid）</li>
 *   <li>生成accessToken和refreshToken（JWT格式，包含相同的sid）</li>
 *   <li>保存用户信息和会话信息到Redis（使用sid作为Hash的field key）</li>
 * </ul>
 * </p>
 *
 * @author wzkris
 * @date 2024/3/11
 */
public abstract class CommonAuthenticationProvider implements AuthenticationProvider {

    private final TokenService tokenService;

    protected CommonAuthenticationProvider(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * 认证核心方法
     * <p>
     * 子类实现具体的认证逻辑，返回CommonAuthenticationToken。
     * </p>
     *
     * @param authentication 认证请求
     * @return 认证结果
     */
    protected abstract CommonAuthenticationToken doAuthenticate(Authentication authentication);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CommonAuthenticationToken authenticationToken = this.doAuthenticate(authentication);

        return this.buildAuthenticationToken(authenticationToken);
    }

    /**
     * 构建认证token
     * <p>
     * 处理流程：
     * <ol>
     *   <li>首次登录：委托 tokenService.login() 生成新 sid 和令牌对</li>
     *   <li>刷新token：委托 tokenService.refresh() 处理轮转/重用策略</li>
     * </ol>
     * </p>
     *
     * @param authenticationToken 认证token
     * @return 构建完成的认证token，包含accessToken和refreshToken
     */
    final CommonAuthenticationToken buildAuthenticationToken(CommonAuthenticationToken authenticationToken) {
        BaseLoginUser loginUser = authenticationToken.getPrincipal();

        TokenPair tokenPair;

        if (authenticationToken.getLoginType() == LoginTypeEnum.REFRESH) {
            tokenPair = tokenService.refresh(loginUser, authenticationToken.getPerms(), authenticationToken.getRefreshToken());
        } else {
            tokenPair = tokenService.login(loginUser, authenticationToken.getPerms());
        }

        authenticationToken.setAccessToken(tokenPair.getAccessToken());
        authenticationToken.setRefreshToken(tokenPair.getRefreshToken());

        return authenticationToken;
    }

}
