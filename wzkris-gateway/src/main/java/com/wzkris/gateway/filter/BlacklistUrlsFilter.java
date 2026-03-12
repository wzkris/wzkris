package com.wzkris.gateway.filter;

import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.TraceIdUtil;
import com.wzkris.gateway.properties.PermitAllProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 网关黑名单拦截 Filter（Servlet 层）。
 * <p>鉴权与 SecurityContext 构建已由 {@code GatewaySecurityContextRepository} 负责，
 * 此处仅根据配置的黑名单路径直接返回 403，避免进入后续处理链。</p>
 *
 * @author wzkris
 */
@Slf4j
@Order(-101)// 在SecurityFilterChain的前面
@Component
@RequiredArgsConstructor
public class BlacklistUrlsFilter extends OncePerRequestFilter {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final PermitAllProperties permitAllProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (isPathDenied(path)) {
            writeJsonResponse(response, HttpStatus.FORBIDDEN,
                    Result.init(BizBaseCodeEnum.ACCESS_DENIED.value(), null, BizBaseCodeEnum.ACCESS_DENIED.desc()));
            return;
        }

        String traceId = TraceIdUtil.generate();
        TraceIdUtil.set(traceId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            TraceIdUtil.clear();
        }
    }

    private static void writeJsonResponse(HttpServletResponse response, HttpStatus status, Object body)
            throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JsonUtil.toJsonString(body));
    }

    private boolean isPathDenied(String path) {
        return CollectionUtils.isNotEmpty(permitAllProperties.getDenys())
                && permitAllProperties.getDenys().stream()
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
    }

}
