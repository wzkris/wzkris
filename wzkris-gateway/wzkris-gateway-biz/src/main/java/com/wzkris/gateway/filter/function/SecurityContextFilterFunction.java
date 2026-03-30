package com.wzkris.gateway.filter.function;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.TraceIdUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
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
@RequiredArgsConstructor
public class SecurityContextFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private static ServerRequest addAuthHeaders(ServerRequest request, Authentication authentication) {
        return ServerRequest.from(request)
                .headers(h -> {
                    Object principal = authentication.getPrincipal();
                    if (principal instanceof BaseLoginUser baseLoginUser) {
                        h.set(CustomHeaderConstants.X_USER_CONTEXT, JsonUtil.toJsonString(baseLoginUser));
                    }
                    if (CollectionUtils.isNotEmpty(authentication.getAuthorities())) {
                        Set<String> permissions = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
                        h.set(CustomHeaderConstants.X_PERMISSIONS, JsonUtil.toJsonString(permissions));
                    }
                    h.set(CustomHeaderConstants.X_TRACING_ID, TraceIdUtil.get());
                })
                .build();
    }

    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {
        Authentication authentication = SecurityUtil.getAuthentication();
        if (!authentication.isAuthenticated()) {
            return ServerResponse.status(401).body(Result.unauth("Unauthorized"));
        }

        ServerRequest requestWithHeaders = addAuthHeaders(request, authentication);
        return next.handle(requestWithHeaders);
    }

}
