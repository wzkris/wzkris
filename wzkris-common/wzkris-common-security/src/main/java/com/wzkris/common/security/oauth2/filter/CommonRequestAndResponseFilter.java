package com.wzkris.common.security.oauth2.filter;

import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.utils.TraceIdUtil;
import com.wzkris.common.security.oauth2.request.RepeatableReadRequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 请求响应处理
 * <p></p>
 * 做一些大家都需要的事情
 *
 * @author wzkris
 */
public class CommonRequestAndResponseFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 使请求体可重复读取
        RepeatableReadRequestWrapper requestWrapper = new RepeatableReadRequestWrapper(request);

        try {
            // 解析TraceID
            final String traceId = requestWrapper.getHeader(HeaderConstants.X_TRACING_ID);
            TraceIdUtil.set(traceId);
            response.setHeader(HeaderConstants.X_TRACING_ID, traceId);

            filterChain.doFilter(requestWrapper, response);
        } finally {
            TraceIdUtil.clear();
        }
    }

}
