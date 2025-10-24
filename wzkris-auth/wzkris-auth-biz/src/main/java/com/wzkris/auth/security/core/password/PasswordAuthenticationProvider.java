package com.wzkris.auth.security.core.password;

import com.wzkris.auth.enums.BizLoginCode;
import com.wzkris.auth.security.config.TokenProperties;
import com.wzkris.auth.security.constants.OAuth2ParameterConstant;
import com.wzkris.auth.security.core.CommonAuthenticationProvider;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.auth.service.TokenService;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.core.enums.BizCaptchaCode;
import com.wzkris.common.core.exception.BaseException;
import com.wzkris.common.core.model.MyPrincipal;
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
 * @description 密码模式核心处理
 */
@Component // 注册成bean方便引用
public final class PasswordAuthenticationProvider extends CommonAuthenticationProvider<PasswordAuthenticationToken> {

    private final List<UserInfoTemplate> userInfoTemplates;

    private final CaptchaService captchaService;

    public PasswordAuthenticationProvider(
            TokenProperties tokenProperties,
            TokenService tokenService,
            JwtEncoder jwtEncoder,
            List<UserInfoTemplate> userInfoTemplates,
            CaptchaService captchaService) {
        super(tokenProperties, tokenService, jwtEncoder);
        this.userInfoTemplates = userInfoTemplates;
        this.captchaService = captchaService;
    }

    @Override
    public PasswordAuthenticationToken doAuthenticate(Authentication authentication) {
        PasswordAuthenticationToken authenticationToken = (PasswordAuthenticationToken) authentication;

        Optional<UserInfoTemplate> templateOptional = userInfoTemplates.stream()
                .filter(t -> t.checkAuthType(authenticationToken.getAuthType()))
                .findFirst();

        if (templateOptional.isEmpty()) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCode.PARAMETER_ERROR.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "invalidParameter.param.invalid",
                    OAuth2ParameterConstant.AUTH_TYPE);
        }

        try {
            // 校验是否被冻结
            captchaService.validateAccount(authenticationToken.getAuthType().getValue() + ":" + authenticationToken.getUsername());
        } catch (BaseException e) {
            OAuth2ExceptionUtil.throwError(e.getBiz(), e.getMessage());
        }

        boolean valid = captchaService.validateChallenge(authenticationToken.getCaptchaId());
        if (!valid) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCaptchaCode.CAPTCHA_ERROR.value(), "invalidParameter.captcha.error");
        }

        MyPrincipal principal = templateOptional.get().loadByUsernameAndPassword(
                authenticationToken.getUsername(), authenticationToken.getPassword());

        if (principal == null) {
            // 抛出异常
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCode.USER_NOT_EXIST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.passlogin.fail");
        }

        return new PasswordAuthenticationToken(authenticationToken.getAuthType(), authenticationToken.getUsername(), principal);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
