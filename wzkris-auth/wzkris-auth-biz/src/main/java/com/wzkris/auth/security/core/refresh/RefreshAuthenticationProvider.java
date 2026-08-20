package com.wzkris.auth.security.core.refresh;

import com.wzkris.auth.domain.TokenClaims;
import com.wzkris.auth.domain.UserSessionContext;
import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.security.core.CommonAuthenticationProvider;
import com.wzkris.auth.service.TokenService;
import com.wzkris.auth.utils.JwtTokenHelper;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.authentication.RoleContextAuthenticationToken;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Component;

/**
 * 刷新模式核心处理
 *
 * @author wzkris
 * @date 2024/3/11
 */
@Component // 注册成bean方便引用
public final class RefreshAuthenticationProvider extends CommonAuthenticationProvider {

    private final TokenService tokenService;

    private final JwtTokenHelper jwtTokenHelper;

    public RefreshAuthenticationProvider(TokenService tokenService, JwtTokenHelper jwtTokenHelper) {
        super(tokenService);
        this.tokenService = tokenService;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @Override
    public UsernamePasswordAuthenticationToken doAuthenticate(Authentication authentication) {
        RefreshAuthenticationToken authenticationToken = (RefreshAuthenticationToken) authentication;
        String refreshToken = authenticationToken.getRefreshToken();
        AuthTypeEnum authType = authenticationToken.getAuthType();

        // 解析 refreshToken 并校验 authType
        TokenClaims claims = parseRefreshToken(refreshToken, authType);

        // 一次 Redis 往返读取会话校验 + 用户信息 + 权限，避免三段独立读取的多往返开销
        UserSessionContext ctx = tokenService.loadUserSessionContext(authType.getValue(), claims.getUid(), claims.getSid());
        if (ctx.revoked() || ctx.userContext().loginUser() == null) {
            // sid 已被拉黑或用户信息不存在
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.AUTHENTICATION_EXPIRED.getCode(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.refresh.fail");
        }

        return RoleContextAuthenticationToken.authenticated(ctx.userContext().loginUser(), null, ctx.userContext().roleContext());
    }

    private TokenClaims parseRefreshToken(String refreshToken, AuthTypeEnum authType) {
        // 从 refreshToken JWT 中解析 uid 和 sid
        TokenClaims claims;
        try {
            claims = jwtTokenHelper.parse(refreshToken);
        } catch (Exception e) {
            // refreshToken 解析失败
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.AUTHENTICATION_EXPIRED.getCode(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.refresh.fail");
            return null;
        }

        if (!StringUtil.equals(authType.getValue(), claims.getAuthType())) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.PARAMETER_ERROR.getCode(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.refresh.fail");
        }
        return claims;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
