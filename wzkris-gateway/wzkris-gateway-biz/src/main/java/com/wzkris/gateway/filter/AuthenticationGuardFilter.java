package com.wzkris.gateway.filter;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 做统一认证兜底。
 */
@Component
@Order(-99)
public class AuthenticationGuardFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityUtil.getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JsonUtil.toJsonString(Result.unauth("未登录访问")));
            return;
        }
        filterChain.doFilter(request, response);
    }

}
