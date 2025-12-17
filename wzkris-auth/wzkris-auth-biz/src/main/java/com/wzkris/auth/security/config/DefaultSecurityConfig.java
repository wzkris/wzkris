/*
 * Copyright 2020-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wzkris.auth.security.config;

import com.wzkris.auth.security.core.CommonAuthenticationConverter;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.auth.security.filter.LoginEndpointFilter;
import com.wzkris.auth.security.filter.LogoutHandlerImpl;
import com.wzkris.auth.service.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import java.util.List;

/**
 * 授权服务器的安全配置
 *
 * @author wzkris
 * @date : 2024/6/14 15:30
 */
@Configuration
public class DefaultSecurityConfig {

    @Bean
    @Order(0)
    public SecurityFilterChain defaultSecurityFilterChain(
            HttpSecurity http,
            List<AuthenticationProvider> authenticationProviders,
            List<CommonAuthenticationConverter<? extends CommonAuthenticationToken>>
                    authenticationConverters,
            TokenService tokenService)
            throws Exception {
        http.securityMatcher("/assets/**", "/activate", "/login", "/logout")
                .csrf(AbstractHttpConfigurer::disable)
                .cors(configurer -> configurer.configure(http))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                )
                .addFilterAt(new LoginEndpointFilter(authenticationProviders, authenticationConverters)
                        , UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> {
                    logout.addLogoutHandler(new LogoutHandlerImpl(tokenService))
                            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT));
                });
        return http.build();
    }

}
