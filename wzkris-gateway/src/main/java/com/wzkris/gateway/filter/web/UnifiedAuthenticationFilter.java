package com.wzkris.gateway.filter.web;

import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.openfeign.exception.RpcException;
import com.wzkris.gateway.config.PermitAllProperties;
import com.wzkris.gateway.service.TokenExtractionService;
import com.wzkris.gateway.utils.ScanAnnotationUrlUtil;
import com.wzkris.gateway.utils.WebFluxUtil;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

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
public class UnifiedAuthenticationFilter implements WebFilter, ApplicationRunner {

    public static final String GATEWAY_PRINCIPAL = "GATEWAY_PRINCIPAL";

    static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final TokenExtractionService tokenExtractionService;

    private final PermitAllProperties permitAllProperties;

    private final Set<String> permitAllAnnotations;

    public static Optional<MyPrincipal> getPrincipal(ContextView contextView) {
        return contextView.getOrEmpty(GATEWAY_PRINCIPAL);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final String path = exchange.getRequest().getPath().value();

        // 1) 黑名单拦截
        if (isPathDenied(path)) {
            return WebFluxUtil.writeResponse(exchange.getResponse(), BizBaseCodeEnum.ACCESS_DENIED);
        }

        // 2) 白名单放行
        if (isPathPermitted(permitAllProperties.getIgnores(), path)
                || isPathPermitted(permitAllAnnotations, path)) {
            return chain.filter(exchange);
        }

        // 3) Token认证流程
        return checkToken(exchange, chain);
    }

    private Mono<Void> checkToken(ServerWebExchange exchange, WebFilterChain chain) {
        return tokenExtractionService.getCurrentPrincipal(exchange.getRequest())
                .flatMap(principal ->
                        chain.filter(
                                        exchange.mutate()
                                                .principal(Mono.just(principal))
                                                .build()
                                )
                                .contextWrite(context -> context.put(GATEWAY_PRINCIPAL, principal))
                )
                .onErrorResume(RpcException.class, rpcException -> {
                    ServerHttpResponse exchangeResponse = exchange.getResponse();
                    exchangeResponse.setRawStatusCode(rpcException.getHttpStatusCode());
                    return WebFluxUtil.writeResponse(exchangeResponse, rpcException.getResult());
                })
                .onErrorResume(throwable -> {
                    ServerHttpResponse exchangeResponse = exchange.getResponse();
                    exchangeResponse.setRawStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return WebFluxUtil.writeResponse(exchangeResponse, BizBaseCodeEnum.SYSTEM_ERROR);
                });
    }

    private boolean isPathPermitted(Collection<String> collections, String url) {
        return collections.stream()
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, url));
    }

    private boolean isPathDenied(String url) {
        return permitAllProperties.getDenys().stream()
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, url));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Set<String> permitAll = ScanAnnotationUrlUtil.scanUrls(PermitAll.class);
        permitAllAnnotations.addAll(permitAll);
    }

}
