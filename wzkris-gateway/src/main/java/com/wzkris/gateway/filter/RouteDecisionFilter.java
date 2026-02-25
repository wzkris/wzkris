package com.wzkris.gateway.filter;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.model.LoginUser;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.loadbalancer.enums.RoutePolicyEnum;
import com.wzkris.gateway.properties.RoutePolicyProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * 路由决策过滤器（Gateway MVC HandlerFilterFunction 实现）。
 * CLOSE：关闭策略；OPEN：按用户 hint 或默认值；FORCE：强制指定 hint。
 *
 * @author wzkris
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RouteDecisionFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private final RoutePolicyProperties routePolicyProperties;

    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {
        RoutePolicyEnum status = getRoutePolicyStatus();

        switch (status) {
            case CLOSE -> {
                return next.handle(request);
            }
            case OPEN -> {
                return handleOpen(request, next);
            }
            case FORCE -> {
                return handleForce(request, next);
            }
        }

        return next.handle(request);
    }

    private ServerResponse handleOpen(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof LoginUser loginUser)) {
                return next.handle(request);
            }

            String userHint = loginUser.getHint();
            String hint = StringUtil.isNotBlank(userHint)
                    ? userHint
                    : routePolicyProperties.getOpenConfig().getDefaultHintValue();

            if (StringUtil.isBlank(hint)) {
                return next.handle(request);
            }

            ServerRequest withHint = ServerRequest.from(request)
                    .headers(h -> h.set(CustomHeaderConstants.X_ROUTE_HINT, hint))
                    .build();
            return next.handle(withHint);
        } catch (Exception e) {
            log.warn("RouteDecisionFilter OPEN 模式处理失败，降级为普通路由: {}", e.getMessage());
            return next.handle(request);
        }
    }

    private ServerResponse handleForce(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {
        String forceHintValue = routePolicyProperties.getForceConfig().getHintValue();
        if (StringUtil.isBlank(forceHintValue)) {
            log.warn("强制路由值为null，降级为CLOSE模式");
            return next.handle(request);
        }

        ServerRequest withHint = ServerRequest.from(request)
                .headers(h -> h.set(CustomHeaderConstants.X_ROUTE_HINT, forceHintValue))
                .build();
        return next.handle(withHint);
    }

    private RoutePolicyEnum getRoutePolicyStatus() {
        RoutePolicyEnum status = routePolicyProperties.getPolicy();
        if (status == null) {
            log.warn("Route policy status is null, defaulting to CLOSE");
            return RoutePolicyEnum.CLOSE;
        }
        return status;
    }

}
