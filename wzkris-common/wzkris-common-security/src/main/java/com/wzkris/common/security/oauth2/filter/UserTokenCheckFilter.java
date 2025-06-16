package com.wzkris.common.security.oauth2.filter;

import com.wzkris.auth.rmi.RmiTokenFeign;
import com.wzkris.auth.rmi.domain.req.TokenReq;
import com.wzkris.auth.rmi.domain.resp.TokenResponse;
import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.security.oauth2.handler.AuthenticationEntryPointImpl;
import com.wzkris.common.security.utils.SecurityUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 用户token校验
 *
 * @author wzkris
 */
public class UserTokenCheckFilter extends OncePerRequestFilter {

    private final RequestHeaderRequestMatcher requestMatcher = new RequestHeaderRequestMatcher(HeaderConstants.X_USER_TOKEN);

    private final RmiTokenFeign rmiTokenFeign;

    private final AuthenticationEntryPoint authenticationEntryPoint = new AuthenticationEntryPointImpl();

    public UserTokenCheckFilter(RmiTokenFeign rmiTokenFeign) {
        this.rmiTokenFeign = rmiTokenFeign;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader(HeaderConstants.X_USER_TOKEN);

        try {
            TokenResponse tokenResponse = rmiTokenFeign.checkUserToken(new TokenReq(token));

            if (!tokenResponse.isSuccess()) {
                authenticationEntryPoint.commence(request, response, new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_TOKEN));
                return;
            }

            SecurityUtil.setAuthentication(new UsernamePasswordAuthenticationToken(tokenResponse.getPrincipal(), null, null));
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            this.authenticationEntryPoint.commence(request, response,
                    new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, ex.getMessage(), null)));
        }

    }

}
