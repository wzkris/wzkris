package com.wzkris.gateway.filter.function;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.gateway.filter.BlacklistUrlsFilter;
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
 * 白名单与 Token 校验已由 Servlet 层 {@link BlacklistUrlsFilter} 完成并设置 SecurityContext。
 *
 * @author wzkris
 */
@Component
@RequiredArgsConstructor
public class SecurityContextFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {
        Authentication authentication = SecurityUtil.getAuthentication();
        if (authentication == null) {
            return next.handle(request);
        }

        ServerRequest requestWithHeaders = addAuthHeaders(request, authentication);
        return next.handle(requestWithHeaders);
    }

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
                })
                .build();
    }

}
