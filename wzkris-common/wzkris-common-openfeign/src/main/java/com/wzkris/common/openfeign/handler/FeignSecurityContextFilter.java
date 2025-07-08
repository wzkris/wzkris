package com.wzkris.common.openfeign.handler;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.openfeign.constants.FeignHeaderConstant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@ConditionalOnClass(UsernamePasswordAuthenticationToken.class)
public class FeignSecurityContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String contextStr = request.getHeader(FeignHeaderConstant.X_SECURITY_CONTEXT);
        if (StringUtil.isNotBlank(contextStr)) {
            SecurityContext securityContext = AuthenticationTokenUtil
                    .deserialize(contextStr, SecurityContext.class);
            SecurityContextHolder.setContext(securityContext);
        }
        filterChain.doFilter(request, response);
    }

}
