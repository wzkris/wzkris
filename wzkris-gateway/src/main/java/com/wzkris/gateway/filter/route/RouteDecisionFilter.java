package com.wzkris.gateway.filter.route;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.loadbalancer.config.RoutePolicyProperties;
import com.wzkris.common.loadbalancer.enums.RoutePolicyStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

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
        RoutePolicyStatusEnum status = getRoutePolicyStatus();

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
                .map(principal -> (MyPrincipal) principal)
                .map(MyPrincipal::getHint)
                .filter(hint -> {
                    if (StringUtil.isNotBlank(hint)) {
                        return true;
                    }
                    hint = routePolicyProperties.getDefaultHint();
                    return StringUtil.isNotBlank(hint);
                })
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
        String forceHint = routePolicyProperties.getForceHint();
        if (StringUtil.isBlank(forceHint)) {
            log.warn("Force routing is enabled but forceHint is empty, fallback to CLOSE mode");
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest().mutate()
                .header(CustomHeaderConstants.X_ROUTE_HINT, forceHint)
                .build();
        return chain.filter(exchange.mutate().request(request).build());
    }

    /**
     * 安全地获取路由策略状态
     */
    private RoutePolicyStatusEnum getRoutePolicyStatus() {
        return Optional.ofNullable(routePolicyProperties.getStatus())
                .flatMap(status -> {
                    return Optional.ofNullable(RoutePolicyStatusEnum.fromValue(status));
                })
                .orElseGet(() -> {
                    log.warn("Invalid route policy status: {}, defaulting to CLOSE", routePolicyProperties.getStatus());
                    return RoutePolicyStatusEnum.CLOSE;
                });
    }

}