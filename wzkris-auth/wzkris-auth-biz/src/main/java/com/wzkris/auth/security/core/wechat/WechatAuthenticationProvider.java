package com.wzkris.auth.security.core.wechat;

import com.wzkris.auth.security.config.TokenProperties;
import com.wzkris.auth.security.constants.OAuth2ParameterConstant;
import com.wzkris.auth.security.core.CommonAuthenticationProvider;
import com.wzkris.auth.service.TokenService;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 短信模式核心处理
 */
@Component
public final class WechatAuthenticationProvider extends CommonAuthenticationProvider<WechatAuthenticationToken> {

    private final List<UserInfoTemplate> userInfoTemplates;

    public WechatAuthenticationProvider(
            TokenProperties tokenProperties,
            TokenService tokenService,
            JwtEncoder jwtEncoder,
            List<UserInfoTemplate> userInfoTemplates) {
        super(tokenProperties, tokenService, jwtEncoder);
        this.userInfoTemplates = userInfoTemplates;
    }

    @Override
    public WechatAuthenticationToken doAuthenticate(Authentication authentication) {
        WechatAuthenticationToken authenticationToken = (WechatAuthenticationToken) authentication;

        Optional<UserInfoTemplate> templateOptional = userInfoTemplates.stream()
                .filter(t -> t.checkAuthenticatedType(authenticationToken.getAuthenticatedType()))
                .findFirst();

        if (templateOptional.isEmpty()) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizCode.BAD_REQUEST.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "request.param.error",
                    OAuth2ParameterConstant.USER_TYPE);
            return null; // never run this line
        }

        CorePrincipal principal = templateOptional
                .get()
                .loadUserByWechat(authenticationToken.getIdentifierType(), authenticationToken.getWxCode());

        if (principal == null) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.wxlogin.fail");
        }

        return new WechatAuthenticationToken(authenticationToken.getAuthenticatedType(), authenticationToken.getIdentifierType(), principal);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
