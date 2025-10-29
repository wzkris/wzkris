package com.wzkris.gateway.filter.web;

import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.openfeign.exception.RpcException;
import com.wzkris.gateway.config.PermitAllProperties;
import com.wzkris.gateway.service.TokenExtractionService;
import com.wzkris.gateway.utils.WebFluxUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.Optional;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 统一身份认证过滤器
 * @date : 2025/01/29
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@RequiredArgsConstructor
public class UnifiedAuthenticationFilter implements WebFilter {

    public static final String GATEWAY_PRINCIPAL = "GATEWAY_PRINCIPAL";

    static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final TokenExtractionService tokenExtractionService;

    private final PermitAllProperties permitAllProperties;

    public static Optional<MyPrincipal> getPrincipal(ContextView contextView) {
        return contextView.getOrEmpty(GATEWAY_PRINCIPAL);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final String path = exchange.getRequest().getPath().value();

        // 1) 黑名单拦截
        if (isPathDenied(path)) {
            return WebFluxUtil.writeResponse(exchange.getResponse(), BizBaseCode.FORBID);
        }

        // 2) 白名单放行
        if (isPathPermitted(path)) {
            return chain.filter(exchange);
        }

        // 3) Token认证流程
        return tokenExtractionService.getCurrentPrincipal(exchange.getRequest())
                .flatMap(principal ->
                        chain.filter(
                                        exchange.mutate()
                                                .principal(Mono.just(principal))
                                                .build()
                                )
                                .contextWrite(context -> context.put(GATEWAY_PRINCIPAL, principal))
                )
                .onErrorResume(RpcException.class, rpcException -> WebFluxUtil.writeResponse(exchange.getResponse(), rpcException.getResult()))
                .onErrorResume(throwable -> WebFluxUtil.writeResponse(exchange.getResponse(), BizBaseCode.INTERNAL_ERROR));
    }

    private boolean isPathPermitted(String url) {
        return permitAllProperties.getIgnores().stream()
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, url));
    }

    private boolean isPathDenied(String url) {
        return permitAllProperties.getDenys().stream()
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, url));
    }

}
