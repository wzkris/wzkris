package com.wzkris.common.security.utils;

import com.wzkris.common.core.utils.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import java.util.regex.Pattern;

/**
 * 统一的 Bearer Token 提取工具。
 * <p>
 * 规则集中管理，避免网关与下游服务实现不一致。
 * </p>
 */
public final class BearerTokenUtil {

    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * 仅允许 system websocket 握手路径使用 query token。
     * <p>
     * 说明：浏览器 WebSocket 无法自定义请求头，因此握手阶段需要一个可用的 token 传递方式。
     * </p>
     */
    private static final Pattern WS_PATH_PATTERN = Pattern.compile("^/wzkris-system-api/ws/.*$");

    private BearerTokenUtil() {
    }

    /**
     * 从 Authorization 头提取 Bearer token。
     */
    public static String extractBearerToken(HttpServletRequest request) {
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
     * WebSocket 握手场景从 query 提取 access_token。
     * <p>
     * 仅在 Upgrade=websocket 且路径满足白名单时返回。
     * </p>
     */
    public static String extractWsAccessToken(HttpServletRequest request) {
        if (!isWebSocketHandshake(request)) {
            return null;
        }
        String uri = request.getRequestURI();
        if (StringUtil.isBlank(uri) || !WS_PATH_PATTERN.matcher(uri).matches()) {
            return null;
        }
        String token = request.getParameter(OAuth2ParameterNames.ACCESS_TOKEN);
        return StringUtil.isNotBlank(token) ? token : null;
    }

    private static boolean isWebSocketHandshake(HttpServletRequest request) {
        String upgrade = request.getHeader("Upgrade");
        return "websocket".equalsIgnoreCase(upgrade);
    }

}
