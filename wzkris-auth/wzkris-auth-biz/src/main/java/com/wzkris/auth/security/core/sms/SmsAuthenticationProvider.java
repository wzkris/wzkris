package com.wzkris.auth.security.core.sms;

import com.wzkris.auth.constants.OAuth2ParameterConstant;
import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.security.core.CommonAuthenticationProvider;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.auth.service.LoginUserService;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.enums.BizCaptchaCodeEnum;
import com.wzkris.common.core.exception.BaseException;
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
public final class SmsAuthenticationProvider extends CommonAuthenticationProvider {

    private final List<LoginUserService> loginUserServices;

    private final CaptchaService captchaService;

    public SmsAuthenticationProvider(
            TokenService tokenService,
            List<LoginUserService> loginUserServices,
            CaptchaService captchaService) {
        super(tokenService);
        this.loginUserServices = loginUserServices;
        this.captchaService = captchaService;
    }

    @Override
    public CommonAuthenticationToken doAuthenticate(Authentication authentication) {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;

        Optional<LoginUserService> templateOptional = loginUserServices.stream()
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

        CommonAuthenticationToken token = templateOptional.get().loadUserByPhoneNumber(authenticationToken.getPhoneNumber());

        if (token == null) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.USER_NOT_EXIST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.smslogin.fail");
        }

        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
