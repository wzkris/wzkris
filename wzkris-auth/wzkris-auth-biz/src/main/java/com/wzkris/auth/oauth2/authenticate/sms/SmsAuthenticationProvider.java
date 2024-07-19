package com.wzkris.auth.oauth2.authenticate.sms;

import com.wzkris.auth.oauth2.authenticate.CommonAuthenticationProvider;
import com.wzkris.auth.oauth2.authenticate.CommonAuthenticationToken;
import com.wzkris.auth.oauth2.model.UserModel;
import com.wzkris.auth.oauth2.service.impl.AppUserDetailsService;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.common.security.oauth2.constants.OAuth2Type;
import com.wzkris.common.security.oauth2.domain.OAuth2User;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 短信模式核心处理
 */
@Component
public class SmsAuthenticationProvider extends CommonAuthenticationProvider<CommonAuthenticationToken> {

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
    public OAuth2AuthenticationToken doAuthenticate(Authentication authentication) {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        // 校验验证码
        captchaService.validateSmsCode(authenticationToken.getPhoneNumber(), authenticationToken.getSmsCode());

        UserModel userModel = userDetailsService.loadUserByPhoneNumber(authenticationToken.getPhoneNumber());

        if (userModel == null) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.smslogin.fail");
        }

        String clientId = ((OAuth2ClientAuthenticationToken) authenticationToken.getPrincipal()).getPrincipal().toString();

        OAuth2User oAuth2User = new OAuth2User(OAuth2Type.APP_USER.getValue(), userModel.getUsername(),
                userModel.getDetails(), userModel.getAuthorities());

        return new OAuth2AuthenticationToken(oAuth2User,
                AuthorityUtils.createAuthorityList(authenticationToken.getScopes()), clientId);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
