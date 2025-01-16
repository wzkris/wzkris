package com.wzkris.monitor.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * 监控权限配置
 *
 * @author wzkris
 */
@EnableWebSecurity
public class WebSecurityConfigurer {

    private final String userContextPath;

    public WebSecurityConfigurer(AdminServerProperties adminServerProperties) {
        this.userContextPath = adminServerProperties.getContextPath();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(userContextPath + "/");

        return httpSecurity.headers(headers -> {
                    headers.defaultsDisabled()
                            .cacheControl(withDefaults())
                            .frameOptions(withDefaults());
                }).authorizeHttpRequests(auth -> {
                    auth.requestMatchers(userContextPath + "/assets/**"
                                    , userContextPath + "/login"
                                    , userContextPath + "/actuator/**"
                                    , userContextPath + "/instances/**").permitAll()
                            .anyRequest().authenticated();
                })
                .formLogin(from -> {
                    from.loginPage(userContextPath + "/login")
                            .successHandler(successHandler);
                }).logout(logout -> logout.logoutUrl(userContextPath + "/logout"))
                .httpBasic(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}
