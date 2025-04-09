package com.wzkris.auth.oauth2.core.password;

import cloud.tianai.captcha.spring.plugins.secondary.SecondaryVerificationApplication;
import com.wzkris.auth.oauth2.core.CommonAuthenticationProvider;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.captcha.service.CaptchaService;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.BaseException;
import com.wzkris.common.security.oauth2.domain.AuthBaseUser;
import com.wzkris.common.security.oauth2.enums.LoginType;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 密码模式核心处理
 */
@Component //注册成bean方便引用
public final class PasswordAuthenticationProvider extends CommonAuthenticationProvider<PasswordAuthenticationToken> {

    private final UserInfoTemplate userInfoTemplate;

    private final CaptchaService captchaService;

    private final SecondaryVerificationApplication application;

    public PasswordAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                          OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                          List<UserInfoTemplate> userInfoTemplates,
                                          CaptchaService captchaService,
                                          SecondaryVerificationApplication application) {
        super(authorizationService, tokenGenerator);
        this.userInfoTemplate = userInfoTemplates.stream()
                .filter(t -> t.checkLoginType(LoginType.SYSTEM_USER))
                .findFirst().get();
        this.captchaService = captchaService;
        this.application = application;
    }

    @Override
    public UsernamePasswordAuthenticationToken doAuthenticate(Authentication authentication) {
        PasswordAuthenticationToken authenticationToken = (PasswordAuthenticationToken) authentication;

        try {
            // 校验是否被冻结
            captchaService.validateLock(authenticationToken.getUsername());
        } catch (BaseException e) {
            OAuth2ExceptionUtil.throwError(e.getBiz(), e.getMessage());
        }

        boolean valid = application.secondaryVerification(authenticationToken.getCaptchaId());
        if (!valid) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.PRECONDITION_FAILED.value(), "captcha.error");
        }

        AuthBaseUser baseUser = userInfoTemplate.loadByUsernameAndPassword(authenticationToken.getUsername(), authenticationToken.getPassword());

        if (baseUser == null) {
            // 抛出异常
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.passlogin.fail");
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(baseUser, null, null);
        usernamePasswordAuthenticationToken.setDetails(authenticationToken.getDetails());
        return usernamePasswordAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
