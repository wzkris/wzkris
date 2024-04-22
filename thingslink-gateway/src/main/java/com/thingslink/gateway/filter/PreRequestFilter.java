package com.thingslink.gateway.filter;

import com.thingslink.common.core.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 请求前拦截器，转发微服务前处理请求
 * @date : 2023/4/13 15:22
 */
@Slf4j
@Component
public class PreRequestFilter implements GlobalFilter, Ordered {

    /**
     * do something
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        mutate.headers(header -> {
            // 清洗请求头
            header.remove(SecurityConstants.GATEWAY_IP_HEADER);
            header.remove(SecurityConstants.INNER_REQUEST_HEADER);
        });
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
