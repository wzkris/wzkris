package com.wzkris.auth.security.core.password;

import com.wzkris.auth.constants.OAuth2ParameterConstant;
import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.remote.interfaces.captchachallenge.ICaptchaChallengeRemote;
import com.wzkris.auth.remote.interfaces.captchachallenge.request.ValidateChallengeRequest;
import com.wzkris.auth.security.core.CommonAuthenticationProvider;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.auth.service.LoginUserService;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.enums.BizCaptchaCodeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.ResultUtil;
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
 * @description 密码模式核心处理
 */
@Component // 注册成bean方便引用
public final class PasswordAuthenticationProvider extends CommonAuthenticationProvider {

    private final List<LoginUserService> loginUserServices;

    private final ICaptchaChallengeRemote captchaChallengeRemote;

    public PasswordAuthenticationProvider(
            TokenService tokenService,
            List<LoginUserService> loginUserServices,
            ICaptchaChallengeRemote captchaChallengeRemote) {
        super(tokenService);
        this.loginUserServices = loginUserServices;
        this.captchaChallengeRemote = captchaChallengeRemote;
    }

    @Override
    public CommonAuthenticationToken doAuthenticate(Authentication authentication) {
        PasswordAuthenticationToken authenticationToken = (PasswordAuthenticationToken) authentication;

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

        Result<Boolean> booleanResult = captchaChallengeRemote.validateChallenge(new ValidateChallengeRequest(authenticationToken.getCaptchaId()));
        boolean pass = ResultUtil.check(booleanResult) && Boolean.TRUE.equals(booleanResult.getData());

        if (!pass) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCaptchaCodeEnum.CAPTCHA_ERROR.value(), CustomErrorCodes.VALIDATE_ERROR,
                    "invalidParameter.captcha.error");
        }

        CommonAuthenticationToken token = (CommonAuthenticationToken) templateOptional.get().loadByUsernameAndPassword(
                authenticationToken.getUsername(), authenticationToken.getPassword());

        if (token == null) {
            // 抛出异常
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.USER_NOT_EXIST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.passlogin.fail");
        }

        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}

