package com.wzkris.common.loadbalancer.filter;

import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.loadbalancer.core.HintContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RequiredArgsConstructor
public class HintContextFilter extends OncePerRequestFilter {

    private final LoadBalancerProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String hint = request.getHeader(properties.getHintHeaderName());

        // 不为空且不为默认版本号
        boolean bool = hint != null && !StringUtil.equals(hint, SecurityConstants.DEFAULT_VERSION);
        try {
            if (bool) {
                HintContextHolder.set(hint);
                response.setHeader(properties.getHintHeaderName(), hint);
            }
            filterChain.doFilter(request, response);
        } finally {
            if (bool) {
                HintContextHolder.clear();
            }
        }
    }

}
