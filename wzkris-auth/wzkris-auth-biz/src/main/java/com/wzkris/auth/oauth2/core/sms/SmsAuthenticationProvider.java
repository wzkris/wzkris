package com.wzkris.auth.oauth2.core.sms;

import com.wzkris.auth.oauth2.core.CommonAuthenticationProvider;
import com.wzkris.auth.oauth2.service.AppUserDetailsService;
import com.wzkris.auth.oauth2.service.UserDetailsServiceExt;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.common.security.oauth2.domain.WzUser;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
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

    private final UserDetailsServiceExt userDetailsService;
    private final CaptchaService captchaService;

    public SmsAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                     OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                     AppUserDetailsService userDetailsService,
                                     CaptchaService captchaService) {
        super(tokenGenerator, authorizationService);
        this.userDetailsService = userDetailsService;
        this.captchaService = captchaService;
    }

    @Override
    public UsernamePasswordAuthenticationToken doAuthenticate(Authentication authentication) {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        // 校验最大次数
        captchaService.validateMaxTryCount(authenticationToken.getPhoneNumber());
        // 校验验证码
        captchaService.validateSmsCode(authenticationToken.getPhoneNumber(), authenticationToken.getSmsCode());

        WzUser wzUser = userDetailsService.loadUserByPhoneNumber(authenticationToken.getPhoneNumber());

        if (wzUser == null) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.smslogin.fail");
        }

        UsernamePasswordAuthenticationToken wzAuthenticationToken = new UsernamePasswordAuthenticationToken(wzUser, null, null);
        wzAuthenticationToken.setDetails(authenticationToken.getDetails());
        return wzAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
