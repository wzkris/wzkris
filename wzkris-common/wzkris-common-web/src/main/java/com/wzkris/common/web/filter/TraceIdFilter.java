package com.wzkris.common.web.filter;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.core.utils.TraceIdUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * <p></p>
 * traceId请求响应处理
 *
 * @author wzkris
 */
public class TraceIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 解析TraceID
            String traceId = request.getHeader(CustomHeaderConstants.X_TRACING_ID);
            traceId = StringUtil.isNotBlank(traceId) ? traceId : TraceIdUtil.generate();
            TraceIdUtil.set(traceId);
            String hint = request.getHeader(CustomHeaderConstants.X_ROUTE_HINT);
            TraceIdUtil.setHint(hint);

            response.setHeader(CustomHeaderConstants.X_TRACING_ID, traceId);
            if (StringUtil.isNotBlank(hint)) {
                response.setHeader(CustomHeaderConstants.X_ROUTE_HINT, hint);
            }
            filterChain.doFilter(request, response);
        } finally {
            TraceIdUtil.clear();
        }
    }

}
