package com.wzkris.auth.security.core.refresh;

import com.wzkris.auth.domain.TokenClaims;
import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.security.core.CommonAuthenticationProvider;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

/**
 * 刷新模式核心处理
 *
 * @author wzkris
 * @date 2024/3/11
 */
@Component // 注册成bean方便引用
public final class RefreshAuthenticationProvider extends CommonAuthenticationProvider {

    private final TokenService tokenService;

    public RefreshAuthenticationProvider(TokenService tokenService) {
        super(tokenService);
        this.tokenService = tokenService;
    }

    @Override
    public CommonAuthenticationToken doAuthenticate(Authentication authentication) {
        RefreshAuthenticationToken authenticationToken = (RefreshAuthenticationToken) authentication;
        String refreshToken = authenticationToken.getRefreshToken();
        String authType = authenticationToken.getAuthType().getValue();

        Long uid = checkParameter(refreshToken, authType);

        // 从存储中加载用户信息
        BaseLoginUser loginUser = tokenService.loadLoginUserByUid(authType, uid);
        if (loginUser == null) {
            // 抛出异常
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.AUTHENTICATION_EXPIRED.getCode(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.refresh.fail");
        }

        // 从存储中加载权限信息
        Set<String> perms = tokenService.loadPermissionsByUid(authType, uid);

        // 如果权限不存在，使用空集合
        if (perms == null) {
            perms = Collections.emptySet();
        }

        CommonAuthenticationToken commonAuthenticationToken = new CommonAuthenticationToken(loginUser, perms, LoginTypeEnum.REFRESH);
        commonAuthenticationToken.setRefreshToken(refreshToken);
        return commonAuthenticationToken;
    }

    private Long checkParameter(String refreshToken, String authType) {
        // 从 refreshToken JWT 中解析 uid 和 sid
        TokenClaims claims;
        try {
            claims = tokenService.parseJwt(refreshToken);
        } catch (Exception e) {
            // refreshToken 解析失败
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.AUTHENTICATION_EXPIRED.getCode(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.refresh.fail");
            return null;
        }

        if (!StringUtil.equals(authType, claims.getAuthType())) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.PARAMETER_ERROR.getCode(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.refresh.fail");
        }

        // 检查 sid 是否在黑名单中
        if (tokenService.isRevoked(authType, claims.getUid(), claims.getSid())) {
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
