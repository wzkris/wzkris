package com.wzkris.common.security.oauth2;

import com.wzkris.auth.feign.token.TokenFeign;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.openfeign.constants.FeignHeaderConstant;
import com.wzkris.common.security.config.PermitAllProperties;
import com.wzkris.common.security.oauth2.converter.CustomJwtAuthenticationConverter;
import com.wzkris.common.security.oauth2.handler.AccessDeniedHandlerImpl;
import com.wzkris.common.security.oauth2.handler.AuthenticationEntryPointImpl;
import com.wzkris.common.security.oauth2.repository.RmiSecurityContextRepository;
import com.wzkris.common.security.oauth2.service.PasswordEncoderDelegate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextRepository;

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
public class ResourceServerConfig {

    private final PermitAllProperties permitAllProperties;

    @Bean
    @RefreshScope
    public SecurityFilterChain resourceSecurityFilterChain(
            HttpSecurity http,
            SecurityContextRepository securityContextRepository,
            JwtDecoder jwtDecoder)
            throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(configurer -> configurer.configure(http))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .securityContext(securityContextConfigurer -> securityContextConfigurer
                        .securityContextRepository(securityContextRepository)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(permitAllProperties.getIgnores().toArray(String[]::new))
                        .permitAll()
                        .requestMatchers(request ->
                                Boolean.parseBoolean(StringUtil.defaultIfBlank(request.getHeader(FeignHeaderConstant.X_FEIGN_REQUEST), "false")))
                        .permitAll()
                        .requestMatchers("/actuator/**")
                        .hasAuthority("SCOPE_monitor")
                        .anyRequest()
                        .authenticated()
                )
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(jwtDecoder)
                                .jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())
                        )
                        .authenticationEntryPoint(new AuthenticationEntryPointImpl())
                        .accessDeniedHandler(new AccessDeniedHandlerImpl())
                )
                .exceptionHandling(exceptionHandler -> exceptionHandler
                        .authenticationEntryPoint(new AuthenticationEntryPointImpl())
                        .accessDeniedHandler(new AccessDeniedHandlerImpl())
                );

        return http.build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository(TokenFeign tokenFeign, JwtDecoder jwtDecoder) {
        return new RmiSecurityContextRepository(tokenFeign, jwtDecoder);
    }

    @Bean
    @RefreshScope
    @ConditionalOnMissingBean(JwtDecoder.class)
    public JwtDecoder decoder() {
        return NimbusJwtDecoder.withJwkSetUri(
                "http://localhost:9000/oauth2/jwks").build();
    }

    @Bean
    public PasswordEncoderDelegate passwordEncoder() {
        return new PasswordEncoderDelegate();
    }

}
