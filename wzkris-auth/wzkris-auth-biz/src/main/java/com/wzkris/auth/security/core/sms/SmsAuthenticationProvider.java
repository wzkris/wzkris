package com.wzkris.auth.security.core.sms;

import com.wzkris.auth.enums.BizLoginCode;
import com.wzkris.auth.security.config.TokenProperties;
import com.wzkris.auth.security.constants.OAuth2ParameterConstant;
import com.wzkris.auth.security.core.CommonAuthenticationProvider;
import com.wzkris.auth.service.TokenService;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.captcha.service.CapService;
import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.core.exception.BaseException;
import com.wzkris.common.security.exception.CustomErrorCodes;
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
 * @description 短信模式核心处理
 */
@Component
public final class SmsAuthenticationProvider extends CommonAuthenticationProvider<SmsAuthenticationToken> {

    private final List<UserInfoTemplate> userInfoTemplates;

    private final CapService capService;

    public SmsAuthenticationProvider(
            TokenProperties tokenProperties,
            TokenService tokenService,
            JwtEncoder jwtEncoder,
            List<UserInfoTemplate> userInfoTemplates,
            CapService capService) {
        super(tokenProperties, tokenService, jwtEncoder);
        this.userInfoTemplates = userInfoTemplates;
        this.capService = capService;
    }

    @Override
    public SmsAuthenticationToken doAuthenticate(Authentication authentication) {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;

        Optional<UserInfoTemplate> templateOptional = userInfoTemplates.stream()
                .filter(t -> t.checkAuthType(authenticationToken.getAuthType()))
                .findFirst();

        if (templateOptional.isEmpty()) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCode.PARAMETER_ERROR.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "invalidParameter.param.invalid",
                    OAuth2ParameterConstant.USER_TYPE);
        }

        try {
            // 校验是否被冻结
            capService.validateAccount(authenticationToken.getPhoneNumber());
            // 校验验证码
            capService.validateCaptcha(
                    authenticationToken.getPhoneNumber(), authenticationToken.getSmsCode());
        } catch (BaseException e) {
            OAuth2ExceptionUtil.throwError(e.getBiz(), CustomErrorCodes.VALIDATE_ERROR, e.getMessage());
        }

        CorePrincipal principal = templateOptional.get().loadUserByPhoneNumber(authenticationToken.getPhoneNumber());

        if (principal == null) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCode.USER_NOT_EXIST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.smslogin.fail");
        }

        return new SmsAuthenticationToken(authenticationToken.getAuthType(), authenticationToken.getPhoneNumber(), principal);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
