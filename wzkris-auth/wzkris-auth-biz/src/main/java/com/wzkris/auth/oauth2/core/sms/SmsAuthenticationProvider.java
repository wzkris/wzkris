package com.wzkris.auth.oauth2.core.sms;

import com.wzkris.auth.oauth2.constants.OAuth2ParameterConstant;
import com.wzkris.auth.oauth2.core.CommonAuthenticationProvider;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.auth.service.UserInfoTemplate;
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
                                     List<UserInfoTemplate> userInfoTemplates,
                                     OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                     CaptchaService captchaService) {
        super(tokenGenerator, authorizationService);
        this.userInfoTemplates = userInfoTemplates;
        this.captchaService = captchaService;
    }

    @Override
    public UsernamePasswordAuthenticationToken doAuthenticate(Authentication authentication) {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        // 校验最大次数
        captchaService.validateMaxTryCount(authenticationToken.getPhoneNumber());
        // 校验验证码
        captchaService.validateSmsCode(authenticationToken.getPhoneNumber(), authenticationToken.getSmsCode());

        Optional<UserInfoTemplate> templateOptional = userInfoTemplates.stream()
                .filter(t -> t.checkLoginType(authenticationToken.getLoginType()))
                .findFirst();

        if (templateOptional.isEmpty()) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "request.param.error", OAuth2ParameterConstant.USER_TYPE);
            return null;// never run this line
        }

        AuthBaseUser baseUser = templateOptional.get().loadUserByPhoneNumber(authenticationToken.getPhoneNumber());

        if (baseUser == null) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.smslogin.fail");
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(baseUser, null, null);
        usernamePasswordAuthenticationToken.setDetails(authenticationToken.getDetails());
        return usernamePasswordAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
