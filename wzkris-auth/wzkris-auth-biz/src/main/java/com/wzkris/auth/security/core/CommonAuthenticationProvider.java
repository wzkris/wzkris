package com.wzkris.auth.security.core;

import com.wzkris.auth.domain.TokenClaims;
import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.properties.TokenProperties;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.model.BaseLoginUser;
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

    private final TokenProperties tokenProperties;

    protected CommonAuthenticationProvider(TokenService tokenService, TokenProperties tokenProperties) {
        this.tokenService = tokenService;
        this.tokenProperties = tokenProperties;
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
        BaseLoginUser loginUser = authenticationToken.getPrincipal();

        String refreshToken;
        String sid;

        if (authenticationToken.getLoginType() == LoginTypeEnum.REFRESH) {
            refreshToken = authenticationToken.getRefreshToken();
            TokenClaims claims = tokenService.parseJwt(refreshToken);
            sid = claims.getSid();
            if (tokenProperties.getReuseRefreshTokens()) {
                // 重用模式：保持 sid，不轮转，仅在接近过期时重新生成 refresh token
                Instant exp = claims.getExpiresAt();
                if (ChronoUnit.HOURS.between(Instant.now(), exp) < 2) {
                    refreshToken = tokenService.generateRefreshToken(loginUser, sid);
                }
                // 保存/延长会话
                tokenService.save(loginUser, sid, authenticationToken.getPerms());
            } else {
                // 轮转模式：生成新的 sid 与新的 refresh token，保存新会话并撤销旧会话
                sid = UUID.randomUUID().toString();
                refreshToken = tokenService.generateRefreshToken(loginUser, sid);
                // 保存新会话
                tokenService.save(loginUser, sid, authenticationToken.getPerms());
                // 撤销旧会话以立即使旧 refresh 无效
                tokenService.revoke(loginUser.getAuthType().getValue(), loginUser.getUid(), claims.getSid());
            }
        } else {
            // 首次登录时，生成新的sid并保存会话
            sid = UUID.randomUUID().toString();
            refreshToken = tokenService.generateRefreshToken(loginUser, sid);
            tokenService.save(loginUser, sid, authenticationToken.getPerms());
        }

        // 生成accessToken，使用确定后的 sid
        String generatedToken = tokenService.generateAccessToken(loginUser, sid);
        authenticationToken.setAccessToken(generatedToken);
        authenticationToken.setRefreshToken(refreshToken);

        return authenticationToken;
    }

}
