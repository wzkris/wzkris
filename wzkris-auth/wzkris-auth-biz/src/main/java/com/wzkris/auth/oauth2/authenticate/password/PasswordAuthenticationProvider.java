package com.wzkris.auth.oauth2.authenticate.password;

import com.wzkris.auth.oauth2.authenticate.CommonAuthenticationProvider;
import com.wzkris.auth.oauth2.authenticate.CommonAuthenticationToken;
import com.wzkris.auth.oauth2.model.UserModel;
import com.wzkris.auth.oauth2.service.impl.SysUserDetailsService;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.common.security.oauth2.authentication.WkAuthenticationToken;
import com.wzkris.common.security.oauth2.constants.OAuth2Type;
import com.wzkris.common.security.oauth2.domain.OAuth2User;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 密码模式核心处理
 */
@Component //注册成bean方便引用
public class PasswordAuthenticationProvider extends CommonAuthenticationProvider<CommonAuthenticationToken> {
    private final SysUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CaptchaService captchaService;

    public PasswordAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                          OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                          SysUserDetailsService userDetailsService,
                                          PasswordEncoder passwordEncoder,
                                          CaptchaService captchaService) {
        super(tokenGenerator, authorizationService);
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.captchaService = captchaService;
    }

    @Override
    public WkAuthenticationToken doAuthenticate(Authentication authentication) {
        PasswordAuthenticationToken authenticationToken = (PasswordAuthenticationToken) authentication;
        // 校验最大次数
        captchaService.validateMaxTryCount(authenticationToken.getPassword());

        UserModel userModel = userDetailsService.loadUserByUsername(authenticationToken.getUsername());

        if (userModel == null || !passwordEncoder.matches(authenticationToken.getPassword(), userModel.getPassword())) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.passlogin.fail");
        }

        String clientId = ((OAuth2ClientAuthenticationToken) authenticationToken.getPrincipal()).getPrincipal().toString();

        OAuth2User oAuth2User = new OAuth2User(OAuth2Type.SYS_USER.getValue(), userModel.getUsername(),
                userModel.getPrincipal(), userModel.getAuthorities());

        return new WkAuthenticationToken(oAuth2User,
                AuthorityUtils.createAuthorityList(authenticationToken.getScopes()), clientId);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
