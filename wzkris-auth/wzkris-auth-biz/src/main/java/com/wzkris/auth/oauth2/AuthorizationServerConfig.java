package com.wzkris.auth.oauth2;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.wzkris.auth.oauth2.config.JwtSecretProperties;
import com.wzkris.auth.oauth2.core.device.DeviceClientAuthenticationConverter;
import com.wzkris.auth.oauth2.core.device.DeviceClientAuthenticationProvider;
import com.wzkris.auth.oauth2.core.password.PasswordAuthenticationConverter;
import com.wzkris.auth.oauth2.core.password.PasswordAuthenticationProvider;
import com.wzkris.auth.oauth2.core.sms.SmsAuthenticationConverter;
import com.wzkris.auth.oauth2.core.sms.SmsAuthenticationProvider;
import com.wzkris.auth.oauth2.core.wechat.WechatAuthenticationConverter;
import com.wzkris.auth.oauth2.core.wechat.WechatAuthenticationProvider;
import com.wzkris.auth.oauth2.customize.CustomTokenClaimsCustomizer;
import com.wzkris.auth.oauth2.dao.CustomDaoAuthenticationProvider;
import com.wzkris.auth.oauth2.handler.AuthenticationFailureHandlerImpl;
import com.wzkris.auth.oauth2.handler.AuthenticationSuccessHandlerImpl;
import com.wzkris.auth.oauth2.utils.JwkUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 认证服务器配置
 * @date : 2024/2/23 11:12
 */
@Slf4j
@Configuration
public class AuthorizationServerConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationFilterChain(
            HttpSecurity http,
            AuthorizationServerSettings serverSettings,
            DeviceClientAuthenticationProvider deviceClientAuthenticationProvider,
            PasswordAuthenticationProvider passwordAuthenticationProvider,
            WechatAuthenticationProvider wechatAuthenticationProvider,
            SmsAuthenticationProvider smsAuthenticationProvider)
            throws Exception {
        // @formatter:off
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();
        http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, Customizer.withDefaults())
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated());

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .tokenEndpoint(
                        tokenEndpoint -> { // 自定义认证授权端点
                            tokenEndpoint
                                    .accessTokenRequestConverters(
                                            authenticationConverters -> { // 从请求拿到对应参数组装成authentication
                                                authenticationConverters.add(new PasswordAuthenticationConverter());
                                                authenticationConverters.add(new SmsAuthenticationConverter());
                                                authenticationConverters.add(new WechatAuthenticationConverter());
                                            })
                                    .authenticationProviders(
                                            authenticationProviders -> { // 校验authentication是否合法
                                                authenticationProviders.add(passwordAuthenticationProvider);
                                                authenticationProviders.add(smsAuthenticationProvider);
                                                authenticationProviders.add(wechatAuthenticationProvider);
                                            })
                                    .accessTokenResponseHandler(new AuthenticationSuccessHandlerImpl()) // 登录成功处理器
                                    .errorResponseHandler(new AuthenticationFailureHandlerImpl()) // 登录失败处理器
                            ;
                        })
                .deviceAuthorizationEndpoint(
                        deviceAuthorizationEndpoint -> // 自定义设备码验证页面
                        deviceAuthorizationEndpoint
                                .verificationUri("/activate")
                                .errorResponseHandler(new AuthenticationFailureHandlerImpl()))
                .deviceVerificationEndpoint(
                        deviceVerificationEndpoint -> // 自定义设备授权页面
                        deviceVerificationEndpoint
                                .consentPage("/oauth2/consent")
                                .errorResponseHandler(new AuthenticationFailureHandlerImpl()))
                .clientAuthentication(
                        clientAuthentication -> { // 客户端认证
                            clientAuthentication
                                    .authenticationConverter(new DeviceClientAuthenticationConverter(
                                            serverSettings.getDeviceAuthorizationEndpoint()))
                                    .authenticationProvider(deviceClientAuthenticationProvider)
                                    .errorResponseHandler(new AuthenticationFailureHandlerImpl());
                        })
                .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint.consentPage("/oauth2/consent"))
                .oidc(Customizer.withDefaults()); // Enable OpenID Connect 1.0

        http.exceptionHandling(exceptionHandler -> {
            exceptionHandler.defaultAuthenticationEntryPointFor(
                    new LoginUrlAuthenticationEntryPoint("/login"), // 401了跳转到登录页
                    new MediaTypeRequestMatcher(MediaType.TEXT_HTML));
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

    @Bean
    public ProviderManager providerManager(CustomDaoAuthenticationProvider daoAuthenticationProvider) {
        return new ProviderManager(daoAuthenticationProvider);
    }

    /**
     * 动态 JWKSource（支持密钥轮换）
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource(JwtSecretProperties properties) throws Exception {
        // 加载当前活跃的密钥对（可通过配置中心动态切换）
        RSAKey rsaKey = JwkUtils.load(properties.getPublicKey(), properties.getPrivateKey());

        // 历史密钥（用于平滑轮换）
//        ECKey previousKey = loadECKey("2", publicKeyResource2, privateKeyResource2);

        // 构建 JWK Set（包含当前和历史密钥）
        JWKSet jwkSet = new JWKSet(List.of(rsaKey));
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    @RefreshScope
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    @RefreshScope
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * 令牌生成规则实现 </br>
     *
     * @return OAuth2TokenGenerator
     */
    @Bean
    public OAuth2TokenGenerator<? extends OAuth2Token> oAuth2TokenGenerator(JwtEncoder jwtEncoder) {
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        jwtGenerator.setJwtCustomizer(new CustomTokenClaimsCustomizer());
        return new DelegatingOAuth2TokenGenerator(
                new OAuth2AccessTokenGenerator(),
                new OAuth2RefreshTokenGenerator(),
                jwtGenerator);
    }
}
