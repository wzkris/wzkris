package com.wzkris.common.log.filter;

import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.utils.TraceIdUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 日志追踪过滤器
 * @date : 2025/01/13 10:30
 */
@Slf4j
@Component
public class TracingLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 网关转发从头拿
        String traceId = request.getHeader(HeaderConstants.X_TRACING_ID);
        TraceIdUtil.set(traceId);

        response.setHeader(HeaderConstants.X_TRACING_ID, TraceIdUtil.get());

        try {
            filterChain.doFilter(request, response);
        } finally {
            TraceIdUtil.clear();
        }
    }

}
