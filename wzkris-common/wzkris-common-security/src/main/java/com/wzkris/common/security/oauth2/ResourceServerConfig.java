package com.wzkris.common.security.oauth2;

import cn.hutool.core.convert.Convert;
import com.wzkris.auth.rmi.RmiTokenFeign;
import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.security.config.PermitAllProperties;
import com.wzkris.common.security.oauth2.handler.AccessDeniedHandlerImpl;
import com.wzkris.common.security.oauth2.handler.AuthenticationEntryPointImpl;
import com.wzkris.common.security.oauth2.introspector.OAuth2OpaqueTokenIntrospector;
import com.wzkris.common.security.oauth2.repository.RmiSecurityContextRepository;
import com.wzkris.common.security.oauth2.service.PasswordEncoderDelegate;
import com.wzkris.common.security.oauth2.service.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
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

    static final DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();

    static {
        bearerTokenResolver.setAllowFormEncodedBodyParameter(true);
        bearerTokenResolver.setAllowUriQueryParameter(true);
    }

    private final PermitAllProperties permitAllProperties;

    private final RmiTokenFeign rmiTokenFeign;

    private ProviderManager opaqueProviderManager;

    private ProviderManager jwtProviderManager;

    @Bean
    @RefreshScope
    public SecurityFilterChain resourceSecurityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(configurer -> configurer.configure(http))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .securityContext(securityContextConfigurer -> {
                    securityContextConfigurer.securityContextRepository(new RmiSecurityContextRepository(rmiTokenFeign));
                })
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(permitAllProperties.getIgnores().toArray(String[]::new))
                        .permitAll()
                        .requestMatchers(request -> Convert.toBool(request.getHeader(HeaderConstants.X_INNER_REQUEST), false))
                        .permitAll()
                        .requestMatchers("/actuator/**")
                        .hasAuthority("SCOPE_monitor")
                        .anyRequest()
                        .authenticated()
                )
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .bearerTokenResolver(bearerTokenResolver)
                        .authenticationManagerResolver(this::getAuthenticationManager)
                        .authenticationEntryPoint(new AuthenticationEntryPointImpl())
                        .accessDeniedHandler(new AccessDeniedHandlerImpl())
                )
                .exceptionHandling(exceptionHandler -> {
                    exceptionHandler.authenticationEntryPoint(new AuthenticationEntryPointImpl())
                            .accessDeniedHandler(new AccessDeniedHandlerImpl());
                });

        return http.build();
    }

    private AuthenticationManager getAuthenticationManager(HttpServletRequest request) {
        String token = bearerTokenResolver.resolve(request);
        if (token.split("\\.").length == 3) {
            if (jwtProviderManager == null) {
                NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(
                                "http://localhost:9000/oauth2/jwks")
                        .build();
                jwtProviderManager = new ProviderManager(new JwtAuthenticationProvider(jwtDecoder));
            }
            return jwtProviderManager;
        } else {
            if (opaqueProviderManager == null) {
                opaqueProviderManager = new ProviderManager(
                        new OpaqueTokenAuthenticationProvider(
                                new OAuth2OpaqueTokenIntrospector(rmiTokenFeign)
                        )
                );
            }
            return opaqueProviderManager;
        }
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
