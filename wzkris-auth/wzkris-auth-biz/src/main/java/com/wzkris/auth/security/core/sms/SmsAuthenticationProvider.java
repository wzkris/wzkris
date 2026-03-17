package com.wzkris.auth.security.core.sms;

import com.wzkris.auth.constants.OAuth2ParameterConstant;
import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.security.core.CommonAuthenticationProvider;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.auth.service.LoginUserService;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.enums.BizCaptchaCodeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.ResultUtil;
import com.wzkris.common.security.exception.CustomErrorCodes;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import com.wzkris.captcha.httpclient.common.CaptchaClient;
import com.wzkris.captcha.httpclient.common.req.CaptchaCheckReq;
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

    private final CaptchaClient captchaClient;

    public SmsAuthenticationProvider(
            TokenService tokenService,
            List<LoginUserService> loginUserServices,
            CaptchaClient captchaClient) {
        super(tokenService);
        this.loginUserServices = loginUserServices;
        this.captchaClient = captchaClient;
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

        CaptchaCheckReq req = new CaptchaCheckReq();
        req.setKey(authenticationToken.getPhoneNumber());
        req.setValue(authenticationToken.getSmsCode());
        Result<Boolean> result = captchaClient.check(req);
        boolean pass = ResultUtil.check(result) && Boolean.TRUE.equals(result.getData());

        if (!pass) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCaptchaCodeEnum.CAPTCHA_ERROR.value(), CustomErrorCodes.VALIDATE_ERROR,
                    "invalidParameter.captcha.error");
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
