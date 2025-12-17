package com.wzkris.auth.security.filter;

import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.security.core.CommonAuthenticationConverter;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.auth.security.handler.AuthenticationSuccessHandlerImpl;
import com.wzkris.common.security.handler.AuthenticationEntryPointImpl;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 登录端点
 *
 * @author wzkris
 * @date : 2024/6/14 16:30
 */
@Slf4j
public class LoginEndpointFilter extends OncePerRequestFilter {

    private final PathPatternRequestMatcher requestMatcher = PathPatternRequestMatcher.withDefaults()
            .matcher(HttpMethod.POST, "/login");

    private final AuthenticationManager authenticationManager;

    private final List<CommonAuthenticationConverter<? extends CommonAuthenticationToken>> authenticationConverters;

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    private final AuthenticationFailureHandler jsonFailureHandler =
            new AuthenticationEntryPointFailureHandler(new AuthenticationEntryPointImpl());

    private final AuthenticationSuccessHandler authenticationSuccessHandler
            = new AuthenticationSuccessHandlerImpl();

    private final AuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler =
            new SavedRequestAwareAuthenticationSuccessHandler();

    public LoginEndpointFilter(
            List<AuthenticationProvider> providers,
            List<CommonAuthenticationConverter<? extends CommonAuthenticationToken>> converters
    ) {
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
            CommonAuthenticationToken commonAuthenticationToken = findSupportedToken(request);

            Authentication authentication = this.authenticationManager.authenticate(commonAuthenticationToken);

            this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        } catch (OAuth2AuthenticationException ex) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace(LogMessage.format("Login request failed: %s", ex.getError()), ex);
            }
            this.jsonFailureHandler.onAuthenticationFailure(request, response, ex);
        } catch (Exception ex) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace(LogMessage.format("Login request failed: %s", ex.getMessage()), ex);
            }
            this.jsonFailureHandler.onAuthenticationFailure(request, response,
                    new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, ex.getMessage(), null)));
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    /**
     * 查找支持当前请求的认证转换器并转换为Token
     *
     * @param request HTTP请求
     * @return 转换后的Token，如果未找到则返回空Optional
     */
    private CommonAuthenticationToken findSupportedToken(HttpServletRequest request) {
        Optional<CommonAuthenticationToken> optional = authenticationConverters.stream()
                .map(converter -> converter.convert(request))
                .filter(Objects::nonNull)
                .findFirst();

        if (optional.isEmpty()) {
            OAuth2ExceptionUtil.throwErrorI18n(BizLoginCodeEnum.LOGIN_TYPE_ERROR.value(), "oauth2.unsupport.logintype");
        }

        CommonAuthenticationToken commonAuthenticationToken = optional.get();
        commonAuthenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));

        return commonAuthenticationToken;
    }

}
