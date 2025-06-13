package com.wzkris.common.security.oauth2;

import cn.hutool.core.convert.Convert;
import com.wzkris.auth.rmi.RmiTokenFeign;
import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.security.config.PermitAllProperties;
import com.wzkris.common.security.oauth2.handler.AccessDeniedHandlerImpl;
import com.wzkris.common.security.oauth2.handler.AuthenticationEntryPointImpl;
import com.wzkris.common.security.oauth2.handler.CustomOpaqueTokenIntrospector;
import com.wzkris.common.security.oauth2.resolver.CustomBearerTokenResolver;
import com.wzkris.common.security.oauth2.service.PasswordEncoderDelegate;
import com.wzkris.common.security.oauth2.service.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 资源服务器安全配置
 * @date : 2023/11/16 10:48
 */
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
@RequiredArgsConstructor
public final class ResourceServerConfig {

    private final PermitAllProperties permitAllProperties;

    private final CustomBearerTokenResolver resolver = new CustomBearerTokenResolver();

    private final RmiTokenFeign rmiTokenFeign;

    private ProviderManager opaqueProviderManager;

    private ProviderManager jwtProviderManager;

    @Bean
    @RefreshScope
    public SecurityFilterChain resourceSecurityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        // 初始化认证管理器
        initAuthenticationProviders();

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(configurer -> configurer.configure(http))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(permitAllProperties.getIgnores().toArray(String[]::new))
                        .permitAll()
                        .requestMatchers("/assets/**", "/login", "/activate")
                        .permitAll()
                        .requestMatchers(request -> Convert.toBool(request.getHeader(HeaderConstants.X_INNER_REQUEST), false))
                        .permitAll()
                        .requestMatchers("/actuator/**")
                        .hasAuthority("SCOPE_monitor")
                        .anyRequest()
                        .authenticated()
                )
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .authenticationManagerResolver(this::getAuthenticationManager)
                        .bearerTokenResolver(resolver)
                        .authenticationEntryPoint(new AuthenticationEntryPointImpl())
                        .accessDeniedHandler(new AccessDeniedHandlerImpl())
                );

        return http.build();
    }

    private void initAuthenticationProviders() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(
                        "http://localhost:9000/oauth2/jwks")
                .build();
        // 初始化不透明令牌认证提供者
        opaqueProviderManager = new ProviderManager(
                new OpaqueTokenAuthenticationProvider(
                        new CustomOpaqueTokenIntrospector(rmiTokenFeign)
                )
        );
        jwtProviderManager = new ProviderManager(new JwtAuthenticationProvider(jwtDecoder));
    }

    private AuthenticationManager getAuthenticationManager(HttpServletRequest request) {
        String token = resolver.resolve(request);
        if (isValidJwtToken(token)) {
            return jwtProviderManager;
        }
        return opaqueProviderManager;
    }

    private boolean isValidJwtToken(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        String[] parts = token.split("\\.");
        return parts.length == 3;
    }

    @Bean("ps")
    public PermissionService permissionService() {
        return new PermissionService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoderDelegate();
    }

}
