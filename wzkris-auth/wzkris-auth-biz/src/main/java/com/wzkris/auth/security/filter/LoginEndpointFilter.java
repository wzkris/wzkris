package com.wzkris.auth.security.filter;

import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.security.core.CommonAuthenticationConverter;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.auth.security.handler.AuthenticationSuccessHandlerImpl;
import com.wzkris.common.security.handler.AuthenticationEntryPointImpl;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.stereotype.Component;

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
@Component
public class LoginEndpointFilter extends AbstractAuthenticationProcessingFilter {

    private final List<CommonAuthenticationConverter<? extends CommonAuthenticationToken>> authenticationConverters;

    public LoginEndpointFilter(
            List<AuthenticationProvider> providers,
            List<CommonAuthenticationConverter<? extends CommonAuthenticationToken>> converters
    ) {
        super(PathPatternRequestMatcher.withDefaults()
                .matcher(HttpMethod.POST, "/login"), new ProviderManager(providers));
        this.authenticationConverters = converters;
        setAuthenticationSuccessHandler(new AuthenticationSuccessHandlerImpl());
        setAuthenticationFailureHandler(new AuthenticationEntryPointFailureHandler(new AuthenticationEntryPointImpl()));
        setAllowSessionCreation(false);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, ServletException {
        Authentication authentication = this.findSupportedToken(request);

        Authentication result = this.getAuthenticationManager().authenticate(authentication);
        if (result == null) {
            throw new ServletException("AuthenticationManager should not return null Authentication object.");
        }
        return result;
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
