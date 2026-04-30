package com.wzkris.common.security.utils;

import com.wzkris.common.core.utils.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

/**
 * 统一的 Bearer Token 提取工具。
 * <p>
 * 规则集中管理，避免网关与下游服务实现不一致。
 * </p>
 */
public final class BearerTokenUtil {

    private static final String BEARER_PREFIX = "Bearer ";

    private BearerTokenUtil() {
    }

    /**
     * 从 Authorization 头提取 Bearer token。
     */
    public static String extractHeaderToken(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtil.isBlank(authorization)) {
            return null;
        }
        if (authorization.regionMatches(true, 0, BEARER_PREFIX, 0, BEARER_PREFIX.length())) {
            String token = authorization.substring(BEARER_PREFIX.length()).trim();
            return StringUtil.isNotBlank(token) ? token : null;
        }
        return null;
    }

    /**
     * 从 query 提取 token。
     */
    public static String extractQueryToken(HttpServletRequest request) {
        return request.getParameter(OAuth2ParameterNames.ACCESS_TOKEN);
    }

}
