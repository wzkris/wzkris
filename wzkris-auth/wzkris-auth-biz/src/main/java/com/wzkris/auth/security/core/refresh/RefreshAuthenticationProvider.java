package com.wzkris.auth.security.core.refresh;

import com.wzkris.auth.domain.TokenClaims;
import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.security.core.CommonAuthenticationProvider;
import com.wzkris.auth.service.TokenService;
import com.wzkris.auth.utils.JwtTokenHelper;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.model.RoleContext;
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

        Long uid = checkParameter(refreshToken, authType);

        // 从存储中加载用户信息
        BaseLoginUser loginUser = tokenService.loadLoginUserByUid(authType.getValue(), uid);
        if (loginUser == null) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.AUTHENTICATION_EXPIRED.getCode(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.refresh.fail");
        }

        // 从存储中加载角色上下文
        RoleContext roleContext = tokenService.loadRoleContextByUid(authType.getValue(), uid);

        return RoleContextAuthenticationToken.authenticated(loginUser, null, roleContext);
    }

    private Long checkParameter(String refreshToken, AuthTypeEnum authType) {
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

        // 检查 sid 是否在黑名单中
        if (tokenService.isRevoked(authType.getValue(), claims.getUid(), claims.getSid())) {
            // sid 已被拉黑
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.AUTHENTICATION_EXPIRED.getCode(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.refresh.fail");
        }
        return claims.getUid();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
