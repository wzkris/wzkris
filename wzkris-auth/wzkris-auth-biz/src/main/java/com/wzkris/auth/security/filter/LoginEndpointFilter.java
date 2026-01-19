package com.wzkris.auth.security.filter;

import com.wzkris.auth.security.handler.AuthenticationSuccessHandlerImpl;
import com.wzkris.common.security.handler.AuthenticationEntryPointImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 登录端点
 *
 * @author wzkris
 * @date : 2024/6/14 16:30
 */
@Slf4j
@Component
public class LoginEndpointFilter extends AbstractAuthenticationProcessingFilter {

    public LoginEndpointFilter(
            List<AuthenticationProvider> providers,
            List<AuthenticationConverter> converters
    ) {
        super(PathPatternRequestMatcher.withDefaults()
                .matcher(HttpMethod.POST, "/login"), new ProviderManager(providers));
        setAuthenticationConverter(new DelegatingAuthenticationConverter(converters));
        setAuthenticationSuccessHandler(new AuthenticationSuccessHandlerImpl());
        setAuthenticationFailureHandler(new AuthenticationEntryPointFailureHandler(new AuthenticationEntryPointImpl()));
        setAllowSessionCreation(false);
    }

}
