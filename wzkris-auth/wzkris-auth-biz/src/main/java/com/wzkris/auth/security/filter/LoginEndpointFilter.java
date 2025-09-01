package com.wzkris.auth.security.filter;

import com.wzkris.auth.enums.BizLoginCode;
import com.wzkris.auth.security.core.CommonAuthenticationConverter;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.auth.security.handler.AuthenticationFailureHandlerImpl;
import com.wzkris.auth.security.handler.AuthenticationSuccessHandlerImpl;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * 登录端点
 *
 * @author wzkris
 * @date : 2024/6/14 16:30
 */
public class LoginEndpointFilter extends OncePerRequestFilter {

    private final AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher("/login",
            "POST");

    private final AuthenticationManager authenticationManager;

    private final List<CommonAuthenticationConverter<? extends CommonAuthenticationToken>> authenticationConverters;

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    private final AuthenticationFailureHandler authenticationFailureHandler = new AuthenticationFailureHandlerImpl();

    private final AuthenticationSuccessHandler authenticationSuccessHandler = new AuthenticationSuccessHandlerImpl();

    public LoginEndpointFilter(List<AuthenticationProvider> providers, List<CommonAuthenticationConverter<? extends CommonAuthenticationToken>> converters) {
        this.authenticationManager = new ProviderManager(providers);
        this.authenticationConverters = converters;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            CommonAuthenticationToken commonAuthenticationToken = null;
            for (CommonAuthenticationConverter<? extends CommonAuthenticationToken> converter
                    : authenticationConverters) {
                commonAuthenticationToken = converter.convert(request);
                if (commonAuthenticationToken != null) {
                    break;
                }
            }

            if (commonAuthenticationToken == null) {
                OAuth2ExceptionUtil.throwErrorI18n(BizLoginCode.LOGIN_TYPE_ERROR.value(), "oauth2.unsupport.logintype");
            }

            commonAuthenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));

            Authentication authentication = this.authenticationManager
                    .authenticate(commonAuthenticationToken);

            this.authenticationSuccessHandler.onAuthenticationSuccess(request, response,
                    authentication);
        } catch (OAuth2AuthenticationException ex) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace(LogMessage.format("Login request failed: %s", ex.getError()), ex);
            }
            this.authenticationFailureHandler.onAuthenticationFailure(request, response, ex);
        } catch (Exception ex) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace(LogMessage.format("Login request failed: %s", ex.getMessage()), ex);
            }
            this.authenticationFailureHandler.onAuthenticationFailure(request, response,
                    new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, ex.getMessage(), null)));
        } finally {
            SecurityContextHolder.clearContext();
        }

    }

}
