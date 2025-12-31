package com.wzkris.auth.security.core.wexcx;

import com.wzkris.auth.constants.OAuth2ParameterConstant;
import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.properties.TokenProperties;
import com.wzkris.auth.security.core.CommonAuthenticationProvider;
import com.wzkris.auth.service.TokenService;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.core.model.UserPrincipal;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 微信小程序核心处理
 */
@Component
public final class WexcxAuthenticationProvider extends CommonAuthenticationProvider<WexcxAuthenticationToken> {

    private final List<UserInfoTemplate> userInfoTemplates;

    public WexcxAuthenticationProvider(
            TokenProperties tokenProperties,
            TokenService tokenService,
            List<UserInfoTemplate> userInfoTemplates) {
        super(tokenProperties, tokenService);
        this.userInfoTemplates = userInfoTemplates;
    }

    @Override
    public WexcxAuthenticationToken doAuthenticate(Authentication authentication) {
        WexcxAuthenticationToken authenticationToken = (WexcxAuthenticationToken) authentication;

        Optional<UserInfoTemplate> templateOptional = userInfoTemplates.stream()
                .filter(t -> t.checkAuthType(authenticationToken.getAuthType()))
                .findFirst();

        if (templateOptional.isEmpty()) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.PARAMETER_ERROR.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "invalidParameter.param.invalid",
                    OAuth2ParameterConstant.AUTH_TYPE);
            return null; // never run this line
        }

        UserPrincipal principal = templateOptional
                .get()
                .loadUserByWxXcx(authenticationToken.getWxCode(), authenticationToken.getPhoneCode());

        if (principal == null) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.USER_NOT_EXIST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.wxlogin.fail");
        }

        return new WexcxAuthenticationToken(authenticationToken.getAuthType(), principal);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WexcxAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
