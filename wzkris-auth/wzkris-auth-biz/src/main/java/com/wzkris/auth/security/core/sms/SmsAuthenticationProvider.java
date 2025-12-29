package com.wzkris.auth.security.core.sms;

import com.wzkris.auth.config.TokenProperties;
import com.wzkris.auth.constants.OAuth2ParameterConstant;
import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.security.core.CommonAuthenticationProvider;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.auth.service.TokenService;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.core.enums.BizCaptchaCodeEnum;
import com.wzkris.common.core.exception.BaseException;
import com.wzkris.common.core.model.UserPrincipal;
import com.wzkris.common.security.exception.CustomErrorCodes;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
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
                .filter(t -> t.checkAuthType(authenticationToken.getAuthType()))
                .findFirst();

        if (templateOptional.isEmpty()) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.PARAMETER_ERROR.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "invalidParameter.param.invalid",
                    OAuth2ParameterConstant.AUTH_TYPE);
        }

        // 校验验证码
        boolean pass = captchaService.validateCaptcha(
                authenticationToken.getPhoneNumber(), authenticationToken.getSmsCode());
        if (!pass) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCaptchaCodeEnum.CAPTCHA_ERROR.value(), CustomErrorCodes.VALIDATE_ERROR,
                    "invalidParameter.captcha.error");
        }

        try {
            // 校验是否被冻结
            captchaService.validateAccount(authenticationToken.getAuthType().getValue() + ":" + authenticationToken.getPhoneNumber());
        } catch (BaseException e) {
            OAuth2ExceptionUtil.throwError(e.getBiz(), CustomErrorCodes.VALIDATE_ERROR, e.getMessage());
        }

        UserPrincipal principal = templateOptional.get().loadUserByPhoneNumber(authenticationToken.getPhoneNumber());

        if (principal == null) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.USER_NOT_EXIST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.smslogin.fail");
        }

        return new SmsAuthenticationToken(authenticationToken.getAuthType(), authenticationToken.getPhoneNumber(), principal);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
