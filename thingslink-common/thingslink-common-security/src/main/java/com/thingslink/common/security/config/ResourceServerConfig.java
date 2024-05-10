package com.thingslink.common.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.security.config.handler.AccessDeniedHandlerImpl;
import com.thingslink.common.security.config.handler.AuthenticationEntryPointImpl;
import com.thingslink.common.security.config.handler.BeforeOpaqueTokenFilter;
import com.thingslink.common.security.config.white.PermitIpConfig;
import com.thingslink.common.security.config.white.PermitUrlConfig;
import com.thingslink.common.security.deserializer.GrantedAuthorityDeserializer;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

    private static final Logger log = LoggerFactory.getLogger(ResourceServerConfig.class);
    private final PermitUrlConfig permitUrlConfig;
    private final PermitIpConfig permitIpConfig;
    private final OpaqueTokenIntrospector opaqueTokenIntrospector;

    // json序列化增强
    static {
        ObjectMapper objectMapper = JsonUtil.getObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(GrantedAuthority.class, new GrantedAuthorityDeserializer());

        objectMapper.registerModules(simpleModule);
    }

    @Bean
    public SecurityFilterChain resourceSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .authorizeHttpRequests(authorize -> {
                            // 配置url白名单
                            PermitUrlConfig.Common common = permitUrlConfig.getCommon();
                            if (common != null) {
                                authorize.requestMatchers(common.getUrls().toArray(String[]::new)).permitAll();
                            }
                            PermitUrlConfig.Custom custom = permitUrlConfig.getCustom();
                            if (custom != null) {
                                authorize.requestMatchers(custom.getUrls().toArray(String[]::new)).permitAll();
                            }
                            authorize.anyRequest().authenticated();
                        }
                )
                .formLogin(Customizer.withDefaults())
                .addFilterBefore(new BeforeOpaqueTokenFilter(permitIpConfig), UsernamePasswordAuthenticationFilter.class)
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

