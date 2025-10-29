package com.wzkris.gateway.filter.route;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
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

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 缓存请求体
        if (hasRequestBody(exchange)) {
            return ServerWebExchangeUtils.cacheRequestBody(exchange, (cachedRequest) -> {
                if (cachedRequest == exchange.getRequest()) {
                    log.warn("Request body caching failed, using original request");
                    return chain.filter(exchange);
                }
                return chain.filter(exchange.mutate().request(cachedRequest).build());
            });
        }

        return chain.filter(exchange);
    }

    /**
     * 检查请求是否有请求体
     */
    private boolean hasRequestBody(ServerWebExchange exchange) {
        long contentLength = exchange.getRequest().getHeaders().getContentLength();
        // 根据内容长度判断
        if (contentLength > 0) {
            return true;
        }

        // 对于某些方法，即使没有明确的内容长度，也可能有请求体
        HttpMethod method = exchange.getRequest().getMethod();
        return method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH;
    }

}
