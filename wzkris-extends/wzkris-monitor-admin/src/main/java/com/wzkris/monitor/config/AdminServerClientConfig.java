package com.wzkris.monitor.config;

import com.wzkris.monitor.filter.CustomCsrfFilter;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static io.netty.handler.codec.http.HttpMethod.DELETE;
import static io.netty.handler.codec.http.HttpMethod.POST;
import static org.springframework.security.config.Customizer.withDefaults;

/**
 * 监控权限配置
 *
 * @author wzkris
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class AdminServerClientConfig {

    private final AdminServerProperties properties;

    public AdminServerClientConfig(AdminServerProperties adminServerProperties) {
        this.properties = adminServerProperties;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler =
                new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(properties.path("/"));

        httpSecurity
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                                    properties.path("/assets/**"),
                                    properties.path("/login"),
                                    properties.path("/actuator/info"),
                                    properties.path("/actuator/health"))
                            .permitAll()
                            .dispatcherTypeMatchers(DispatcherType.ASYNC)
                            .permitAll()
                            .anyRequest()
                            .authenticated();
                })
                .formLogin(from -> from.loginPage(properties.path("/login")).successHandler(successHandler))
                .logout(logout -> logout.logoutUrl(properties.path("/logout")))
                .httpBasic(withDefaults());

        httpSecurity
                .addFilterAfter(new CustomCsrfFilter(), BasicAuthenticationFilter.class)
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher(this.properties.path("/instances"), POST.toString()),
                                new AntPathRequestMatcher(this.properties.path("/instances/*"), DELETE.toString()),
                                new AntPathRequestMatcher(this.properties.path("/actuator/**"))));

        return httpSecurity.build();
    }

}
