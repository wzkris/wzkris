package com.wzkris.gateway.filter.function;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.TraceIdUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Set;

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
                    if (authentication instanceof AnonymousAuthenticationToken) {
                        return;
                    }
                    Object principal = authentication.getPrincipal();
                    if (principal instanceof BaseLoginUser baseLoginUser) {
                        h.set(CustomHeaderConstants.X_USER_CONTEXT, JsonUtil.toJsonString(baseLoginUser));
                    }
                    if (CollectionUtils.isNotEmpty(authentication.getAuthorities())) {
                        Set<String> permissions = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
                        h.set(CustomHeaderConstants.X_PERMISSIONS, JsonUtil.toJsonString(permissions));
                    }
                })
                .build();
        return next.handle(newRequest);
    }

}
