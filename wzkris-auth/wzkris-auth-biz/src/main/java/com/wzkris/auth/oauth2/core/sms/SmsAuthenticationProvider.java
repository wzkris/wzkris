package com.wzkris.auth.oauth2.core.sms;

import com.wzkris.auth.oauth2.core.CommonAuthenticationProvider;
import com.wzkris.auth.oauth2.model.UserModel;
import com.wzkris.auth.oauth2.service.AppUserDetailsService;
import com.wzkris.auth.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.common.security.oauth2.constants.OAuth2Type;
import com.wzkris.common.security.oauth2.domain.OAuth2User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 短信模式核心处理
 */
@Component
public final class SmsAuthenticationProvider extends CommonAuthenticationProvider<SmsAuthenticationToken> {

    private final AppUserDetailsService userDetailsService;
    private final CaptchaService captchaService;

    public SmsAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                     OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                     AppUserDetailsService userDetailsService, CaptchaService captchaService) {
        super(tokenGenerator, authorizationService);
        this.userDetailsService = userDetailsService;
        this.captchaService = captchaService;
    }

    @Override
    public UsernamePasswordAuthenticationToken doAuthenticate(Authentication authentication) {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        // 校验验证码
        captchaService.validateSmsCode(authenticationToken.getPhoneNumber(), authenticationToken.getSmsCode());

        UserModel userModel = userDetailsService.loadUserByPhoneNumber(authenticationToken.getPhoneNumber());

        if (userModel == null) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.smslogin.fail");
        }

        OAuth2User oAuth2User = new OAuth2User(OAuth2Type.APP_USER.getValue(), userModel.getUsername(),
                userModel.getPrincipal(), userModel.getAuthorities());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(oAuth2User, null, null);
        usernamePasswordAuthenticationToken.setDetails(authenticationToken.getDetails());
        return usernamePasswordAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
