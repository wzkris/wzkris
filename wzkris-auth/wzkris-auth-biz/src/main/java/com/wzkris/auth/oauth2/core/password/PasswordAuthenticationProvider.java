package com.wzkris.auth.oauth2.core.password;

import com.wzkris.auth.config.CaptchaConfig;
import com.wzkris.auth.oauth2.core.CommonAuthenticationProvider;
import com.wzkris.auth.oauth2.service.SysUserDetailsService;
import com.wzkris.auth.oauth2.service.UserDetailsServiceExt;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.common.security.oauth2.domain.WzUser;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 密码模式核心处理
 */
@Component //注册成bean方便引用
public final class PasswordAuthenticationProvider extends CommonAuthenticationProvider<PasswordAuthenticationToken> {
    private final UserDetailsServiceExt userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CaptchaService captchaService;
    private final CaptchaConfig captchaConfig;

    public PasswordAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                          OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                          SysUserDetailsService userDetailsService,
                                          PasswordEncoder passwordEncoder,
                                          CaptchaService captchaService, CaptchaConfig captchaConfig) {
        super(tokenGenerator, authorizationService);
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.captchaService = captchaService;
        this.captchaConfig = captchaConfig;
    }

    @Override
    public UsernamePasswordAuthenticationToken doAuthenticate(Authentication authentication) {
        PasswordAuthenticationToken authenticationToken = (PasswordAuthenticationToken) authentication;
        // 验证码校验
        if (captchaConfig.getEnabled()) {
            captchaService.validatePicCode(authenticationToken.getUuid(), authenticationToken.getCode());
        }
        // 校验最大次数
        captchaService.validateMaxTryCount(authenticationToken.getUsername());

        WzUser wzUser = userDetailsService.loadUserByUsername(authenticationToken.getUsername());

        if (wzUser == null || !passwordEncoder.matches(authenticationToken.getPassword(), wzUser.getPassword())) {
            // 抛出异常
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.passlogin.fail");
        }

        UsernamePasswordAuthenticationToken wzAuthenticationToken = new UsernamePasswordAuthenticationToken(wzUser, null, null);
        wzAuthenticationToken.setDetails(authenticationToken.getDetails());
        return wzAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
