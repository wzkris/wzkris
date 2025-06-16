package com.wzkris.auth.security.core.password;

import cloud.tianai.captcha.spring.plugins.secondary.SecondaryVerificationApplication;
import com.wzkris.auth.rmi.enums.AuthenticatedType;
import com.wzkris.auth.security.config.TokenProperties;
import com.wzkris.auth.security.core.CommonAuthenticationProvider;
import com.wzkris.auth.service.TokenService;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.captcha.service.CaptchaService;
import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.BaseException;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 密码模式核心处理
 */
@Component // 注册成bean方便引用
public final class PasswordAuthenticationProvider extends CommonAuthenticationProvider<PasswordAuthenticationToken> {

    private final UserInfoTemplate userInfoTemplate;

    private final CaptchaService captchaService;

    private final SecondaryVerificationApplication application;

    public PasswordAuthenticationProvider(TokenProperties tokenProperties,
                                          TokenService tokenService,
                                          List<UserInfoTemplate> userInfoTemplates,
                                          CaptchaService captchaService,
                                          SecondaryVerificationApplication application) {
        super(tokenProperties, tokenService);
        this.userInfoTemplate = userInfoTemplates.stream()
                .filter(t -> t.checkAuthenticatedType(AuthenticatedType.SYSTEM_USER))
                .findFirst()
                .get();
        this.captchaService = captchaService;
        this.application = application;
    }

    @Override
    public PasswordAuthenticationToken doAuthenticate(Authentication authentication) {
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

        CorePrincipal principal = userInfoTemplate.loadByUsernameAndPassword(
                authenticationToken.getUsername(), authenticationToken.getPassword());

        if (principal == null) {
            // 抛出异常
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.passlogin.fail");
        }

        return new PasswordAuthenticationToken(authenticationToken.getUsername(), principal);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
