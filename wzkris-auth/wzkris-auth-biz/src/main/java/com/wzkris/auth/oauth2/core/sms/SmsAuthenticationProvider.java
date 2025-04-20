package com.wzkris.auth.oauth2.core.sms;

import com.wzkris.auth.oauth2.constants.OAuth2ParameterConstant;
import com.wzkris.auth.oauth2.core.CommonAuthenticationProvider;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.captcha.service.CaptchaService;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.BaseException;
import com.wzkris.common.security.oauth2.constants.CustomErrorCodes;
import com.wzkris.common.security.oauth2.domain.AuthBaseUser;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
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

    public SmsAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                     OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                     List<UserInfoTemplate> userInfoTemplates,
                                     CaptchaService captchaService) {
        super(authorizationService, tokenGenerator);
        this.userInfoTemplates = userInfoTemplates;
        this.captchaService = captchaService;
    }

    @Override
    public UsernamePasswordAuthenticationToken doAuthenticate(Authentication authentication) {
        SmsAuthenticationToken smsAuthenticationToken = (SmsAuthenticationToken) authentication;

        Optional<UserInfoTemplate> templateOptional = userInfoTemplates.stream()
                .filter(t -> t.checkLoginType(smsAuthenticationToken.getLoginType()))
                .findFirst();

        if (templateOptional.isEmpty()) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST,
                    "request.param.error", OAuth2ParameterConstant.USER_TYPE);
        }

        try {
            // 校验是否被冻结
            captchaService.validateLock(smsAuthenticationToken.getPhoneNumber());
            // 校验验证码
            captchaService.validateCaptcha(smsAuthenticationToken.getPhoneNumber(), smsAuthenticationToken.getSmsCode());
        } catch (BaseException e) {
            OAuth2ExceptionUtil.throwError(e.getBiz(), CustomErrorCodes.VALIDATE_ERROR, e.getMessage());
        }

        AuthBaseUser baseUser = templateOptional.get().loadUserByPhoneNumber(smsAuthenticationToken.getPhoneNumber());

        if (baseUser == null) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.smslogin.fail");
        }

        Authentication clientPrincipal = (Authentication) authentication.getPrincipal();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(baseUser, null, clientPrincipal.getAuthorities());
        authenticationToken.setDetails(smsAuthenticationToken.getDetails());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
