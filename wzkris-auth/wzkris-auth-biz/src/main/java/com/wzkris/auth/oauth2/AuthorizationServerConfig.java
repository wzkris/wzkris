package com.wzkris.auth.oauth2;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.wzkris.auth.oauth2.authenticate.password.PasswordAuthenticationConverter;
import com.wzkris.auth.oauth2.authenticate.password.PasswordAuthenticationProvider;
import com.wzkris.auth.oauth2.authenticate.sms.SmsAuthenticationConverter;
import com.wzkris.auth.oauth2.authenticate.sms.SmsAuthenticationProvider;
import com.wzkris.auth.oauth2.handler.AuthenticationFailureHandlerImpl;
import com.wzkris.auth.oauth2.handler.AuthenticationSuccessHandlerImpl;
import com.wzkris.auth.oauth2.service.impl.AppUserDetailsService;
import com.wzkris.auth.oauth2.service.impl.SysUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 认证服务器配置
 * @date : 2024/2/23 11:12
 */
@Configuration
public class AuthorizationServerConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationFilterChain(HttpSecurity http,
                                                        SysUserDetailsService sysUserDetailsService,
                                                        AppUserDetailsService appUserDetailsService,
                                                        OAuth2AuthorizationService authorizationService,
                                                        OAuth2TokenGenerator<? extends OAuth2Token> oAuth2TokenGenerator,
                                                        PasswordEncoder passwordEncoder) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .tokenEndpoint(tokenEndpoint -> { // 自定义认证授权端点
                    tokenEndpoint.errorResponseHandler(new AuthenticationFailureHandlerImpl()) // 登录失败处理器
                            .accessTokenResponseHandler(new AuthenticationSuccessHandlerImpl()) // 登录成功处理器
                            .accessTokenRequestConverters(authenticationConverters -> { // 从请求拿到对应参数组装成authentication
                                authenticationConverters.add(new PasswordAuthenticationConverter());
                                authenticationConverters.add(new SmsAuthenticationConverter());
                            })
                            .authenticationProviders(authenticationProviders -> { // 校验authentication是否合法
                                authenticationProviders.add(
                                        new PasswordAuthenticationProvider(authorizationService
                                                , oAuth2TokenGenerator
                                                , sysUserDetailsService
                                                , passwordEncoder));
                                authenticationProviders.add(
                                        new SmsAuthenticationProvider(authorizationService,
                                                oAuth2TokenGenerator,
                                                appUserDetailsService));
                            })
                    ;
                })
                .clientAuthentication(clientAuthentication -> { // 客户端认证
                    clientAuthentication.errorResponseHandler(new AuthenticationFailureHandlerImpl());
                })
                .authorizationEndpoint(authorizationEndpoint -> { // 授权
                    authorizationEndpoint.consentPage("/oauth2/consent");
                })
                .oidc(Customizer.withDefaults());  // Enable OpenID Connect 1.0


        http.exceptionHandling(exceptionHandler -> {
            exceptionHandler.defaultAuthenticationEntryPointFor(
                    new LoginUrlAuthenticationEntryPoint("/login"),// 401了跳转到登录页
                    new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
            );
        });

        return http.build();
    }

    /**
     * 用于授权服务器配置
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    /**
     * 默认的表单AuthenticationProvider，由于存在多个UserDetailsService 自动配置会失败
     * 手动对后台用户进行创建
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder, SysUserDetailsService sysUserDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(sysUserDetailsService);
        return daoAuthenticationProvider;
    }

    /**
     * 令牌生成规则实现 </br>
     *
     * @return OAuth2TokenGenerator
     */
    @Bean
    public OAuth2TokenGenerator<? extends OAuth2Token> oAuth2TokenGenerator(JWKSource<SecurityContext> jwkSource) {
        return new DelegatingOAuth2TokenGenerator(
                new OAuth2AccessTokenGenerator(),
                new OAuth2RefreshTokenGenerator(),
                new JwtGenerator(new NimbusJwtEncoder(jwkSource)));
    }

}
