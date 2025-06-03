package com.wzkris.common.security.oauth2;

import com.wzkris.auth.rmi.RmiTokenService;
import com.wzkris.common.security.config.PermitAllProperties;
import com.wzkris.common.security.oauth2.handler.AccessDeniedHandlerImpl;
import com.wzkris.common.security.oauth2.handler.AuthenticationEntryPointImpl;
import com.wzkris.common.security.oauth2.handler.CustomOpaqueTokenIntrospector;
import com.wzkris.common.security.oauth2.resolver.CustomBearerTokenResolver;
import com.wzkris.common.security.oauth2.service.PasswordEncoderDelegate;
import com.wzkris.common.security.oauth2.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @DubboReference
    private final RmiTokenService rmiTokenService;

    @Bean
    @RefreshScope // 动态更新白名单
    public SecurityFilterChain resourceSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(configurer -> configurer.configure(http))
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .authorizeHttpRequests(authorize -> {
                    // 配置url白名单
                    authorize
                            .requestMatchers(permitAllProperties.getIgnores().toArray(String[]::new))
                            .permitAll()
                            .requestMatchers("/assets/**", "/login", "/activate")
                            .permitAll()
                            .requestMatchers("/actuator/**")
                            .hasAuthority("SCOPE_monitor")
                            .anyRequest()
                            .authenticated();
                })
                .formLogin(Customizer.withDefaults())
                .oauth2ResourceServer(resourceServer -> {
                    resourceServer
                            .opaqueToken(token -> {
                                token.introspector(new CustomOpaqueTokenIntrospector(rmiTokenService));
                            })
                            .bearerTokenResolver(new CustomBearerTokenResolver())
                            .authenticationEntryPoint(new AuthenticationEntryPointImpl())
                            .accessDeniedHandler(new AccessDeniedHandlerImpl());
                });

        return http.build();
    }

    /**
     * 鉴权具体的实现逻辑
     */
    @Bean("ps")
    public PermissionService permissionService() {
        return new PermissionService();
    }

    /**
     * 单例的加密器 给OAuth2其余组件公用
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoderDelegate();
    }
}
