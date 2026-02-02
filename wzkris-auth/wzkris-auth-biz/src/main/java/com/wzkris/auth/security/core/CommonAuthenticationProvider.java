package com.wzkris.auth.security.core;

import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.model.LoginUser;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

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
     *   <li>如果是刷新token，从原refreshToken解析sid并保持使用</li>
     *   <li>如果是首次登录，生成新的sid</li>
     *   <li>使用相同的sid生成accessToken和refreshToken</li>
     *   <li>保存用户信息和会话信息到Redis（使用sid作为Hash的field key）</li>
     * </ol>
     * </p>
     *
     * @param authenticationToken 认证token
     * @return 构建完成的认证token，包含accessToken和refreshToken
     */
    final CommonAuthenticationToken buildAuthenticationToken(CommonAuthenticationToken authenticationToken) {
        LoginUser loginUser = authenticationToken.getPrincipal();
        Long uid = loginUser.getUid();
        String sid;
        String refreshToken = authenticationToken.getRefreshToken();

        if (authenticationToken.getLoginType() == LoginTypeEnum.REFRESH) {
            // 刷新token时，从原refreshToken中解析sid，保持使用相同的sid
            String oldRefreshToken = authenticationToken.getRefreshToken();
            TokenService.TokenInfo tokenInfo = tokenService.parseJwt(oldRefreshToken);
            sid = tokenInfo.getSid();
            Instant exp = tokenInfo.getExp();
            if (ChronoUnit.HOURS.between(exp, Instant.now()) < 1) {
                // 使用原sid生成新的refreshToken
                refreshToken = tokenService.generateRefreshToken(uid, sid);
            }
        } else {
            // 首次登录时，生成新的sid
            sid = UUID.randomUUID().toString();
            refreshToken = tokenService.generateRefreshToken(uid, sid);
        }

        // 生成accessToken，使用相同的sid
        String generatedToken = tokenService.generateAccessToken(uid, sid);
        authenticationToken.setAccessToken(generatedToken);
        authenticationToken.setRefreshToken(refreshToken);

        // 保存用户信息和权限信息（传入sid作为Redis Hash的field key）
        tokenService.save(loginUser, sid, authenticationToken.getPerms());

        return authenticationToken;
    }

}
