package com.thingslink.common.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.security.oauth2.config.OAuth2Properties;
import com.thingslink.common.security.oauth2.config.PermitUrlProperties;
import com.thingslink.common.security.oauth2.deserializer.GrantedAuthorityDeserializer;
import com.thingslink.common.security.oauth2.handler.AccessDeniedHandlerImpl;
import com.thingslink.common.security.oauth2.handler.AuthenticationEntryPointImpl;
import com.thingslink.common.security.oauth2.handler.CustomOpaqueTokenIntrospector;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
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
@AllArgsConstructor
public class ResourceServerConfig {

    private final PermitUrlProperties permitUrlProperties;
    private final OAuth2Properties oAuth2Properties;

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
                            if (permitUrlProperties.getCommonUrls() != null) {
                                authorize.requestMatchers(permitUrlProperties.getCommonUrls().toArray(String[]::new)).permitAll();
                            }
                            if (permitUrlProperties.getCustomUrls() != null) {
                                authorize.requestMatchers(permitUrlProperties.getCustomUrls().toArray(String[]::new)).permitAll();
                            }
                            authorize.anyRequest().authenticated();
                        }
                )
                .formLogin(Customizer.withDefaults())
                .oauth2ResourceServer(resourceServer -> {
                    resourceServer
                            .opaqueToken(token -> {
                                token.introspector(new CustomOpaqueTokenIntrospector(oAuth2Properties.getIntrospectionUri(),
                                        oAuth2Properties.getClientid(), oAuth2Properties.getClientSecret()));
                            })
                            .authenticationEntryPoint(new AuthenticationEntryPointImpl())
                            .accessDeniedHandler(new AccessDeniedHandlerImpl())
                    ;
                })
//                .oauth2Client(Customizer.withDefaults())
        ;

        return http.build();
    }

    /**
     * 单例的加密器 给OAuth2其余组件公用
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

