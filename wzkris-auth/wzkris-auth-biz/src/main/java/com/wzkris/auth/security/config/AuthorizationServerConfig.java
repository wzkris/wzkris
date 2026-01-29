package com.wzkris.auth.security.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.wzkris.auth.properties.JwtSecretProperties;
import com.wzkris.auth.security.handler.Oauth2AuthenticationSuccessHandlerImpl;
import com.wzkris.auth.security.oauth2.customize.CustomTokenClaimsCustomizer;
import com.wzkris.auth.security.oauth2.device.DeviceClientAuthenticationConverter;
import com.wzkris.auth.security.oauth2.device.DeviceClientAuthenticationProvider;
import com.wzkris.auth.security.utils.JwkUtils;
import com.wzkris.common.security.handler.AccessDeniedHandlerImpl;
import com.wzkris.common.security.handler.AuthenticationEntryPointImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.Assert;

import java.util.ArrayList;
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

    private final AuthenticationSuccessHandler authenticationSuccessHandler =
            new Oauth2AuthenticationSuccessHandlerImpl();

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
                        .securityContextRepository(securityContextRepository) // SecurityContextHolderFilter
                )
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .with(authorizationServerConfigurer, authorizationServer -> authorizationServer
                        .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                                .accessTokenResponseHandler(authenticationSuccessHandler) // 登录成功处理器
                                .errorResponseHandler(jsonFailureHandler)
                        )
                        .tokenIntrospectionEndpoint(tokenIntrospectionEndpoint -> tokenIntrospectionEndpoint
                                .introspectionResponseHandler(authenticationSuccessHandler) // token验证端点
                                .errorResponseHandler(jsonFailureHandler)
                        )
                        .tokenRevocationEndpoint(tokenRevocationEndpoint -> tokenRevocationEndpoint
                                .revocationResponseHandler(authenticationSuccessHandler) // token撤销端点
                                .errorResponseHandler(jsonFailureHandler)
                        )
                        .deviceAuthorizationEndpoint(deviceAuthorizationEndpoint -> deviceAuthorizationEndpoint
                                .verificationUri("http://localhost:5777/auth/oauth2/activate")// 自定义设备码验证页面
                                .errorResponseHandler(jsonFailureHandler)
                        )
                        .deviceVerificationEndpoint(deviceVerificationEndpoint -> deviceVerificationEndpoint
                                .consentPage("http://localhost:5777/auth/oauth2/consent")// 设备授权页面
                                .errorResponseHandler(jsonFailureHandler)
                        )
                        .clientAuthentication(clientAuthentication -> clientAuthentication // 客户端认证
                                .authenticationConverter(new DeviceClientAuthenticationConverter(
                                        serverSettings.getDeviceAuthorizationEndpoint()))
                                .authenticationProvider(new DeviceClientAuthenticationProvider(registeredClientRepository))
                                .errorResponseHandler(jsonFailureHandler)
                        )
                        .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
                                .consentPage("http://localhost:5777/auth/oauth2/consent")
                                .errorResponseHandler(jsonFailureHandler)
                        )
                        .oidc(Customizer.withDefaults()) // Enable OpenID Connect 1.0
                )
        .oauth2ResourceServer(resourceServer -> resourceServer
                .authenticationEntryPoint(authenticationEntryPoint) // 处理oauth路径异常
                .accessDeniedHandler(new AccessDeniedHandlerImpl())
        )
        .exceptionHandling(exceptionHandler -> exceptionHandler
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(new AccessDeniedHandlerImpl())
        );

        return http.build();
    }

    /**
     * 缓存的 JWK Set Bean（支持密钥轮换）
     * <p>
     * 刷新策略：
     * <ol>
     *   <li>JwtSecretProperties 使用 @RefreshScope，配置中心刷新时其属性会更新。</li>
     *   <li>本 Bean 使用 @RefreshScope，配置刷新时会重新创建，自动加载新的密钥。</li>
     *   <li>请求路径上大部分时间只是在内存中基于缓存的 JWKSet 做 select，性能开销可控。</li>
     * </ol>
     * </p>
     *
     * @param properties JWT 密钥配置
     * @return 缓存的 JWK Set
     */
    @Bean
    @RefreshScope
    public JWKSet jwkSet(JwtSecretProperties properties) {
        String publicKey = properties.getPublicKey();
        String privateKey = properties.getPrivateKey();
        String previousPublicKey = properties.getPreviousPublicKey();
        String previousPrivateKey = properties.getPreviousPrivateKey();

        Assert.isTrue(publicKey != null && privateKey != null, "JWT 密钥配置不完整：publicKey 和 privateKey 必须配置");

        try {
            List<JWK> keys = new ArrayList<>();

            RSAKey currentKey = JwkUtils.load(publicKey, privateKey);
            keys.add(currentKey);

            if (previousPublicKey != null && previousPrivateKey != null) {
                RSAKey previousKey = JwkUtils.load(previousPublicKey, previousPrivateKey);
                keys.add(previousKey);
            }

            return new JWKSet(keys);
        } catch (Exception e) {
            log.error("加载 JWT 密钥失败: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to load JWT keys", e);
        }
    }

    /**
     * 动态 JWKSource（支持密钥轮换）
     * <p>
     * 使用缓存的 CachedJwkSet Bean，配置刷新时会自动使用新的密钥。
     * </p>
     *
     * @param jwkSet JWK Set
     * @return 动态 JWKSource
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource(JWKSet jwkSet) {
        return (jwkSelector, securityContext) -> {
            return jwkSelector.select(jwkSet);
        };
    }

    /**
     * JWT 编码器配置
     * <p>
     * 设置 JwkSelector 以确保签名时使用当前密钥（第一个密钥）。
     * 这样即使 JWK Set 中包含多个密钥（当前 + 历史），也能明确选择当前密钥进行签名。
     * </p>
     * <p>
     * 注意：kid（Key ID）需要在生成 JWT 时手动设置。
     * NimbusJwtEncoder 不会自动从选中的 JWK 中提取 kid，因此 TokenService 需要
     * 从当前密钥中获取 kid 并显式设置到 JWT header 中。
     * </p>
     *
     * @param jwkSource JWK 源
     * @return JwtEncoder
     */
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        NimbusJwtEncoder encoder = new NimbusJwtEncoder(jwkSource);

        // 设置密钥选择器：优先选择第一个密钥（当前密钥）
        // 这样确保生成新 JWT 时总是使用最新的密钥
        encoder.setJwkSelector((List<JWK> jwks) -> {
            if (jwks.isEmpty()) {
                throw new IllegalStateException("JWK Set 为空，无法选择密钥进行签名");
            }
            // 选择第一个密钥（当前密钥）
            return jwks.get(0);
        });
        
        return encoder;
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
