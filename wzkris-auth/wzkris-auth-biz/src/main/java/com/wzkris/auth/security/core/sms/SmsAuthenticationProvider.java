package com.wzkris.auth.security.core.sms;

import com.wzkris.auth.security.config.TokenProperties;
import com.wzkris.auth.security.constants.OAuth2ParameterConstant;
import com.wzkris.auth.security.core.CommonAuthenticationProvider;
import com.wzkris.auth.service.TokenService;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.captcha.service.CaptchaService;
import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.BaseException;
import com.wzkris.common.security.exception.CustomErrorCodes;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 短信模式核心处理
 */
@Component
public final class SmsAuthenticationProvider extends CommonAuthenticationProvider<SmsAuthenticationToken> {

    private final List<UserInfoTemplate> userInfoTemplates;

    private final CaptchaService captchaService;

    public SmsAuthenticationProvider(
            TokenProperties tokenProperties,
            TokenService tokenService,
            List<UserInfoTemplate> userInfoTemplates,
            CaptchaService captchaService) {
        super(tokenProperties, tokenService);
        this.userInfoTemplates = userInfoTemplates;
        this.captchaService = captchaService;
    }

    @Override
    public SmsAuthenticationToken doAuthenticate(Authentication authentication) {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;

        Optional<UserInfoTemplate> templateOptional = userInfoTemplates.stream()
                .filter(t -> t.checkAuthenticatedType(authenticationToken.getAuthenticatedType()))
                .findFirst();

        if (templateOptional.isEmpty()) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizCode.BAD_REQUEST.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "request.param.error",
                    OAuth2ParameterConstant.USER_TYPE);
        }

        try {
            // 校验是否被冻结
            captchaService.validateLock(authenticationToken.getPhoneNumber());
            // 校验验证码
            captchaService.validateCaptcha(
                    authenticationToken.getPhoneNumber(), authenticationToken.getSmsCode());
        } catch (BaseException e) {
            OAuth2ExceptionUtil.throwError(e.getBiz(), CustomErrorCodes.VALIDATE_ERROR, e.getMessage());
        }

        CorePrincipal principal = templateOptional.get().loadUserByPhoneNumber(authenticationToken.getPhoneNumber());

        if (principal == null) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.smslogin.fail");
        }

        return new SmsAuthenticationToken(authenticationToken.getAuthenticatedType(), authenticationToken.getPhoneNumber(), principal);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
