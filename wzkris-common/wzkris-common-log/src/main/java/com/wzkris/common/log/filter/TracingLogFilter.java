package com.wzkris.common.log.filter;

import cn.hutool.core.util.IdUtil;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.utils.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 日志追踪过滤器
 * @date : 2025/01/13 10:30
 */
@Slf4j
public class TracingLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 网关转发从头拿
        String tracingId = request.getHeader(CommonConstants.X_TRACING_ID);
        if (StringUtil.isBlank(tracingId)) {
            tracingId = IdUtil.fastUUID();
        }
        MDC.put(CommonConstants.X_TRACING_ID, tracingId);
        response.setHeader(CommonConstants.X_TRACING_ID, tracingId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
