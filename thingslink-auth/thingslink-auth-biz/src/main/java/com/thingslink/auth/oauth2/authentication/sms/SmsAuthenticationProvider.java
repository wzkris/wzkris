package com.thingslink.auth.oauth2.authentication.sms;

import com.thingslink.auth.oauth2.authentication.CommonAuthenticationProvider;
import com.thingslink.auth.oauth2.authentication.CommonAuthenticationToken;
import com.thingslink.auth.oauth2.service.AppUserDetailsService;
import com.thingslink.common.security.utils.OAuth2EndpointUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 短信模式核心处理
 */
public class SmsAuthenticationProvider extends CommonAuthenticationProvider<CommonAuthenticationToken> {

    private final AppUserDetailsService userDetailsService;

    public SmsAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                     OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                     AppUserDetailsService userDetailsService) {
        super(tokenGenerator, authorizationService);
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        Assert.notNull(userDetailsService, "userDetailsService cannot be null");
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected UsernamePasswordAuthenticationToken doAuthenticate(Authentication authentication) {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        // TODO 校验code

        UserDetails userDetails = userDetailsService.loadUserByPhoneNumber(authenticationToken.getPhoneNumber());

        if (userDetails == null) {
            OAuth2EndpointUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.smslogin.fail");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
