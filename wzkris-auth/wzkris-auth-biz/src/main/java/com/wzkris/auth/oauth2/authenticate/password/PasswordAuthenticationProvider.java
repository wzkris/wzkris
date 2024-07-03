package com.wzkris.auth.oauth2.authenticate.password;

import com.wzkris.auth.oauth2.authenticate.CommonAuthenticationProvider;
import com.wzkris.auth.oauth2.authenticate.CommonAuthenticationToken;
import com.wzkris.auth.oauth2.model.UserModel;
import com.wzkris.auth.oauth2.service.impl.SysUserDetailsService;
import com.wzkris.common.security.oauth2.constants.OAuth2Type;
import com.wzkris.common.security.oauth2.domain.OAuth2User;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 密码模式核心处理
 */
public class PasswordAuthenticationProvider extends CommonAuthenticationProvider<CommonAuthenticationToken> {
    private final SysUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public PasswordAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                          OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                          SysUserDetailsService userDetailsService,
                                          PasswordEncoder passwordEncoder) {
        super(tokenGenerator, authorizationService);
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        Assert.notNull(userDetailsService, "userDetailsService cannot be null");
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2AuthenticationToken doAuthenticate(Authentication authentication) {
        PasswordAuthenticationToken authenticationToken = (PasswordAuthenticationToken) authentication;

        UserModel userModel = userDetailsService.loadUserByUsername(authenticationToken.getUsername());


        if (userModel == null || !passwordEncoder.matches(authenticationToken.getPassword(), userModel.getPassword())) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.passlogin.fail");
        }

        String clientId = ((OAuth2ClientAuthenticationToken) authenticationToken.getPrincipal()).getPrincipal().toString();

        OAuth2User oAuth2User = new OAuth2User(OAuth2Type.SYS_USER.getValue(), userModel.getUsername(),
                userModel.getDetails(), userModel.getAuthorities());

        return new OAuth2AuthenticationToken(oAuth2User,
                AuthorityUtils.createAuthorityList(authenticationToken.getScopes()), clientId);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
