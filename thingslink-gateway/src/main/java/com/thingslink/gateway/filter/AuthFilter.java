package com.thingslink.gateway.filter;

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
 * @description : 登录过滤器
 * @date : 2023/4/13 15:22
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    /**
     * 验证通过时，将 用户信息 通过 Header 转发给服务
     * 验证不通过，仍然转发给服务。接口是否需要登录的校验交给服务自身处理
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
