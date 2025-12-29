package com.wzkris.gateway.filter.route;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.model.UserPrincipal;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.loadbalancer.enums.RoutePolicyEnum;
import com.wzkris.gateway.config.RoutePolicyProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 路由决策过滤器
 * 状态说明：
 * - CLOSE: 关闭路由策略
 * - OPEN: 开启路由策略，基于用户信息上的hint进行路由
 * - FORCE: 强制路由，所有流量指向指定hint版本
 *
 * @author wzkris
 */
@Slf4j
@Order(-10000)
@Component
@RequiredArgsConstructor
public class RouteDecisionFilter implements GlobalFilter {

    private final RoutePolicyProperties routePolicyProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        RoutePolicyEnum status = getRoutePolicyStatus();

        return switch (status) {
            case CLOSE -> chain.filter(exchange);
            case OPEN -> handleOpenStatus(exchange, chain);
            case FORCE -> handleForceStatus(exchange, chain);
        };
    }

    /**
     * 处理OPEN状态：基于用户hint进行路由
     */
    private Mono<Void> handleOpenStatus(ServerWebExchange exchange, GatewayFilterChain chain) {
        return exchange.getPrincipal()
                .map(principal -> (UserPrincipal) principal)
                .map(UserPrincipal::getHint)
                .map(userHint -> {
                    // 如果用户hint为空，则使用默认hint
                    return StringUtil.isNotBlank(userHint) ? userHint : routePolicyProperties.getOpenConfig().getDefaultHintValue();
                })
                .filter(hint -> StringUtil.isNotBlank(hint))
                .map(hint -> {
                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header(CustomHeaderConstants.X_ROUTE_HINT, hint)
                            .build();
                    return exchange.mutate().request(request).build();
                })
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);
    }

    /**
     * 处理FORCE状态：强制所有流量路由到指定版本
     */
    private Mono<Void> handleForceStatus(ServerWebExchange exchange, GatewayFilterChain chain) {
        String forceHintValue = routePolicyProperties.getForceConfig().getHintValue();
        if (StringUtil.isBlank(forceHintValue)) {
            log.warn("强制路由值为null，降级为CLOSE模式");
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest().mutate()
                .header(CustomHeaderConstants.X_ROUTE_HINT, forceHintValue)
                .build();
        return chain.filter(exchange.mutate().request(request).build());
    }

    /**
     * 安全地获取路由策略状态
     */
    private RoutePolicyEnum getRoutePolicyStatus() {
        RoutePolicyEnum status = routePolicyProperties.getPolicy();
        if (status == null) {
            log.warn("Route policy status is null, defaulting to CLOSE");
            return RoutePolicyEnum.CLOSE;
        }
        return status;
    }

}