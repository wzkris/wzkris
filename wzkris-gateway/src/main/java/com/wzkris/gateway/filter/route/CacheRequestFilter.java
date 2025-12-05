package com.wzkris.gateway.filter.route;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.CacheRequestBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 获取body请求数据（解决流不能重复读取问题）
 *
 * @author wzkris
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Component
public class CacheRequestFilter implements GlobalFilter {

    private final GatewayFilter gatewayFilter;

    public CacheRequestFilter(CacheRequestBodyGatewayFilterFactory cacheRequestBodyGatewayFilterFactory) {
        CacheRequestBodyGatewayFilterFactory.Config config = new CacheRequestBodyGatewayFilterFactory.Config();
        config.setBodyClass(DataBuffer.class);
        this.gatewayFilter = cacheRequestBodyGatewayFilterFactory.apply(config);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return gatewayFilter.filter(exchange, chain);
    }

}
