package com.wzkris.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 获取body请求数据（解决流不能重复读取问题）
 *
 * @author wzkris
 */
@Component
public class CacheRequestFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // GET DELETE 不过滤
        HttpMethod method = exchange.getRequest().getMethod();
        if (method == HttpMethod.GET || method == HttpMethod.DELETE) {
            return chain.filter(exchange);
        }
        return ServerWebExchangeUtils.cacheRequestBodyAndRequest(exchange, (serverHttpRequest) -> {
            if (serverHttpRequest == exchange.getRequest()) {
                return chain.filter(exchange);
            }
            return chain.filter(exchange.mutate().request(serverHttpRequest).build());
        });
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
