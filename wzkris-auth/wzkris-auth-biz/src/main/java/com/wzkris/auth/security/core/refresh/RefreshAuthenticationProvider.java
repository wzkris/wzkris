package com.wzkris.auth.security.core.refresh;

import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.security.config.TokenProperties;
import com.wzkris.auth.security.core.CommonAuthenticationProvider;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Component;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 刷新模式核心处理
 */
@Component // 注册成bean方便引用
public final class RefreshAuthenticationProvider extends CommonAuthenticationProvider<CommonAuthenticationToken> {

    private final TokenService tokenService;

    public RefreshAuthenticationProvider(TokenProperties tokenProperties,
                                         TokenService tokenService,
                                         JwtEncoder jwtEncoder) {
        super(tokenProperties, tokenService, jwtEncoder);
        this.tokenService = tokenService;
    }

    @Override
    public RefreshAuthenticationToken doAuthenticate(Authentication authentication) {
        RefreshAuthenticationToken authenticationToken = (RefreshAuthenticationToken) authentication;

        MyPrincipal principal = tokenService.loadByRefreshToken(authenticationToken.getAuthType().getValue(), authenticationToken.getRefreshToken());
        if (principal == null) {
            // 抛出异常
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.AUTHENTICATION_NOT_EXIST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.refresh.fail");
        }

        return new RefreshAuthenticationToken(authenticationToken.getAuthType(), authenticationToken.getRefreshToken(), principal);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
