package com.wzkris.gateway.filter.function;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.TraceIdUtil;
import com.wzkris.common.security.authentication.RoleContextAuthenticationToken;
import com.wzkris.common.security.utils.SecurityUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 仅负责将 SecurityContext 中的身份/权限追加为请求头，供下游使用。
 *
 * @author wzkris
 */
@Component
public class SecurityContextFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {
        Authentication authentication = SecurityUtil.getAuthentication();
        String gatewayClientIp = ServletUtil.getClientIP(request.servletRequest());
        ServerRequest newRequest = ServerRequest.from(request)
                .headers(h -> {
                    h.set(CustomHeaderConstants.X_TRACING_ID, TraceIdUtil.get());
                    h.set(CustomHeaderConstants.X_GATEWAY_CLIENT_IP, gatewayClientIp);
                    if (!(authentication instanceof RoleContextAuthenticationToken rcToken)
                            || !rcToken.isAuthenticated()) {
                        return;
                    }
                    // HTTP header 仅支持 ASCII，中文需 Base64 编码避免 ISO-8859-1 传输乱码
                    h.set(CustomHeaderConstants.X_USER_CONTEXT,
                            encodeBase64(JsonUtil.toJsonString(rcToken.getPrincipal())));
                    h.set(CustomHeaderConstants.X_ROLE_CONTEXT,
                            encodeBase64(JsonUtil.toJsonString(rcToken.getRoleContext())));
                })
                .build();
        return next.handle(newRequest);
    }

    /**
     * HTTP header 仅支持 ASCII，Base64 编码避免中文等非 ASCII 字符在传输中乱码
     */
    private static String encodeBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

}
