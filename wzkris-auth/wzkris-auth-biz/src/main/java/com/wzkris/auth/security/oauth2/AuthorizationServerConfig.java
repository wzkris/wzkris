package com.wzkris.auth.security.oauth2;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.wzkris.auth.config.JwtSecretProperties;
import com.wzkris.auth.security.handler.AuthenticationSuccessHandlerImpl;
import com.wzkris.auth.security.oauth2.customize.CustomTokenClaimsCustomizer;
import com.wzkris.auth.security.oauth2.device.DeviceClientAuthenticationConverter;
import com.wzkris.auth.security.oauth2.device.DeviceClientAuthenticationProvider;
import com.wzkris.auth.security.oauth2.utils.JwkUtils;
import com.wzkris.common.security.handler.AccessDeniedHandlerImpl;
import com.wzkris.common.security.handler.AuthenticationEntryPointImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 授权服务器的oauth2配置
 * @date : 2024/2/23 11:12
 */
@Slf4j
@Configuration
public class AuthorizationServerConfig {

    private final AuthenticationEntryPoint authenticationEntryPoint = new AuthenticationEntryPointImpl();

    private final AuthenticationFailureHandler jsonFailureHandler =
            new AuthenticationEntryPointFailureHandler(authenticationEntryPoint);

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationFilterChain(
            HttpSecurity http,
            SecurityContextRepository securityContextRepository,
            RegisteredClientRepository registeredClientRepository)
            throws Exception {

        // @formatter:off
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();
        AuthorizationServerSettings serverSettings = AuthorizationServerSettings.builder()
                .build();

        http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .securityContext(securityContextConfigurer -> securityContextConfigurer
                        .securityContextRepository(securityContextRepository)// 需要支持用户token
                )
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .with(authorizationServerConfigurer, authorizationServer -> authorizationServer
                        .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                                .accessTokenResponseHandler(new AuthenticationSuccessHandlerImpl()) // 登录成功处理器
                                .errorResponseHandler(jsonFailureHandler)
                        )
                        .tokenIntrospectionEndpoint(tokenIntrospectionEndpoint -> tokenIntrospectionEndpoint
                                .introspectionResponseHandler(new AuthenticationSuccessHandlerImpl()) // token验证端点
                                .errorResponseHandler(jsonFailureHandler)
                        )
                        .tokenRevocationEndpoint(tokenRevocationEndpoint -> tokenRevocationEndpoint
                                .revocationResponseHandler(new AuthenticationSuccessHandlerImpl()) // token撤销端点
                                .errorResponseHandler(jsonFailureHandler)
                        )
                        .deviceAuthorizationEndpoint(deviceAuthorizationEndpoint -> deviceAuthorizationEndpoint
                                .verificationUri("/activate")// 自定义设备码验证页面
                                .errorResponseHandler(jsonFailureHandler)
                        )
                        .deviceVerificationEndpoint(deviceVerificationEndpoint -> deviceVerificationEndpoint
                                .consentPage("/oauth2/consent")// 自定义设备授权页面
                                .errorResponseHandler(jsonFailureHandler)
                        )
                        .clientAuthentication(clientAuthentication -> clientAuthentication // 客户端认证
                                .authenticationConverter(new DeviceClientAuthenticationConverter(
                                        serverSettings.getDeviceAuthorizationEndpoint()))
                                .authenticationProvider(new DeviceClientAuthenticationProvider(registeredClientRepository))
                                .errorResponseHandler(jsonFailureHandler)
                        )
                        .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
                                .consentPage("/oauth2/consent")
                                .errorResponseHandler(jsonFailureHandler)
                        )
                        .oidc(Customizer.withDefaults()) // Enable OpenID Connect 1.0
                )
        .oauth2ResourceServer(resourceServer -> resourceServer
                .authenticationEntryPoint(authenticationEntryPoint) // 处理oauth路径异常
                .accessDeniedHandler(new AccessDeniedHandlerImpl())
        )
        .exceptionHandling(exceptionHandler -> exceptionHandler
                .defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint("/login"),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
        );

        return http.build();
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
