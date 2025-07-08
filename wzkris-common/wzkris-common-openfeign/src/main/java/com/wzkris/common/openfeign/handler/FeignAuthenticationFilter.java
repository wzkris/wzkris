package com.wzkris.common.openfeign.handler;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.openfeign.constants.FeignHeaderConstant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class FeignAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authenticationStr = request.getHeader(FeignHeaderConstant.X_AUTHENTICATION);
        if (StringUtil.isNotBlank(authenticationStr)) {
            UsernamePasswordAuthenticationToken authenticationToken = AuthenticationTokenUtil
                    .deserialize(authenticationStr, UsernamePasswordAuthenticationToken.class);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

}
