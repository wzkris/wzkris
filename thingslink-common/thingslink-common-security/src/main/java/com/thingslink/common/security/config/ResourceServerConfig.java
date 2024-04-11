package com.thingslink.common.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.thingslink.auth.api.RemoteTokenApi;
import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.security.config.handler.AccessDeniedHandlerImpl;
import com.thingslink.common.security.config.handler.AuthenticationEntryPointImpl;
import com.thingslink.common.security.config.handler.SsoLogoutHandler;
import com.thingslink.common.security.config.handler.SsoLogoutSuccessHandler;
import com.thingslink.common.security.config.white.CommonWhiteConfig;
import com.thingslink.common.security.config.white.CustomWhiteConfig;
import com.thingslink.common.security.deserializer.GrantedAuthorityDeserializer;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 资源服务器安全配置
 * @date : 2023/11/16 10:48
 */
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
@AllArgsConstructor
public class ResourceServerConfig {

    private final CommonWhiteConfig commonWhiteConfig;
    private final CustomWhiteConfig customWhiteConfig;
    private final OpaqueTokenIntrospector opaqueTokenIntrospector;
    private final RemoteTokenApi remoteTokenApi;

    private static final BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();

    // json序列化增强
    static {
        ObjectMapper objectMapper = JsonUtil.getObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(GrantedAuthority.class, new GrantedAuthorityDeserializer());

        objectMapper.registerModules(simpleModule);
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers(commonWhiteConfig.getUrls().toArray(String[]::new)).permitAll()
                                .requestMatchers(customWhiteConfig.getUrls().toArray(String[]::new)).permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .logout(logout -> {
                    logout.addLogoutHandler(new SsoLogoutHandler(bearerTokenResolver, remoteTokenApi))
                            .logoutSuccessHandler(new SsoLogoutSuccessHandler());
                })
                .oauth2ResourceServer(resourceServer -> {
                    resourceServer
                            .opaqueToken(token -> {
                                token.introspector(opaqueTokenIntrospector);
                            })
                            .authenticationEntryPoint(new AuthenticationEntryPointImpl())
                            .accessDeniedHandler(new AccessDeniedHandlerImpl())
                    ;
                })
        ;

        return http.build();
    }
}

