package com.wzkris.gateway.filter.web;

import com.wzkris.gateway.utils.TokenExtractionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 控制器请求过滤器 - 提取用户信息供切面使用
 * @date : 2025/01/29
 */
@Slf4j
//@Component
@RequiredArgsConstructor
public class ControllerRequestFilter implements WebFilter {

    private final TokenExtractionUtil tokenExtractionUtil;

    public static final String GATEWAY_PRINCIPAL = "GATEWAY_PRINCIPAL";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (exchange.getResponse().isCommitted()) {
            return chain.filter(exchange);
        }

        // 使用工具类获取用户信息
        return tokenExtractionUtil.getCurrentPrincipal(exchange.getRequest())
                .flatMap(principal ->
                        chain.filter(exchange.mutate().principal(Mono.just(principal)).build())
                )
                .switchIfEmpty(
                        chain.filter(exchange)
                )
                .onErrorResume(e -> {
                    log.error("提取用户信息异常: {}", e.getMessage());
                    return chain.filter(exchange);
                });
    }

}
