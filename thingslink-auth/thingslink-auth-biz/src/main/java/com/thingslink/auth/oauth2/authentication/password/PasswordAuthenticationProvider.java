package com.thingslink.auth.oauth2.authentication.password;

import com.thingslink.auth.oauth2.authentication.CommonAuthenticationProvider;
import com.thingslink.auth.oauth2.authentication.CommonAuthenticationToken;
import com.thingslink.auth.oauth2.service.SysUserDetailsService;
import com.thingslink.common.security.utils.OAuth2EndpointUtil;
import com.thingslink.common.security.utils.LoginUserUtil;
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
 * @description 密码模式核心处理
 */
public class PasswordAuthenticationProvider extends CommonAuthenticationProvider<CommonAuthenticationToken> {
    private final SysUserDetailsService userDetailsService;

    public PasswordAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                          OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                          SysUserDetailsService userDetailsService) {
        super(tokenGenerator, authorizationService);
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        Assert.notNull(userDetailsService, "userDetailsService cannot be null");
        this.userDetailsService = userDetailsService;
    }

    @Override
    public UsernamePasswordAuthenticationToken doAuthenticate(Authentication authentication) {
        PasswordAuthenticationToken authenticationToken = (PasswordAuthenticationToken) authentication;

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationToken.getUsername());

        if (userDetails == null || !LoginUserUtil.matchesPassword(authenticationToken.getPassword(), userDetails.getPassword())) {
            OAuth2EndpointUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.passlogin.fail");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
