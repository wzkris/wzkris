package com.demo.oauthclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * OAuth2 客户端安全配置
 * <p>
 * 使用 @Order 确保此配置优先于 ResourceServerConfig 执行
 * 使用 securityMatcher 限制只处理 OAuth2 相关的路径
 * </p>
 *
 * @author wzkris
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain oauth2ClientFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // 允许访问的公开端点
                        .requestMatchers("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        // OAuth2 回调端点允许访问
                        .requestMatchers("/login/oauth2/code/**")
                        .permitAll()
                        // 登录页面直接重定向到 OAuth2 授权端点
                        .requestMatchers("/login")
                        .permitAll()
                        // 其他请求需要认证
                        .anyRequest()
                        .authenticated()
                )
                // 启用 OAuth2 登录
                .oauth2Login(oauth2 -> oauth2
                        // 禁用默认的登录页面 UI（避免需要静态资源）
                        .loginPage("/oauth2/authorization/auth-center")
                        // 登录成功后的默认跳转页面
                        .defaultSuccessUrl("/messages", true)
                        // 登录失败处理
                        .failureUrl("/oauth2/authorization/auth-center?error")
                )
                // 启用登出
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                )
                // 禁用 CSRF（如果是 API 服务，可以禁用）
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}

