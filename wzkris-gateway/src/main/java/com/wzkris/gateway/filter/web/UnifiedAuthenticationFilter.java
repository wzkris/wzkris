package com.wzkris.gateway.filter.web;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.exception.service.ResultException;
import com.wzkris.common.core.model.ClientPrincipal;
import com.wzkris.common.core.model.LoginUser;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.gateway.properties.PermitAllProperties;
import com.wzkris.gateway.service.TokenExtractionService;
import com.wzkris.gateway.utils.ScanAnnotationUrlUtil;
import com.wzkris.gateway.utils.WebFluxUtil;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;
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

    static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final TokenExtractionService tokenExtractionService;

    private final PermitAllProperties permitAllProperties;

    private final Set<String> permitAllAnnotations;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final String path = exchange.getRequest().getPath().value();

        // 1) 黑名单拦截
        if (isPathDenied(path)) {
            return WebFluxUtil.writeResponse(exchange.getResponse(), BizBaseCodeEnum.ACCESS_DENIED);
        }

        // 2) 白名单处理
        if (isPermitAllPath(path)) {
            return chain.filter(exchange);
        }

        // 3) Token认证流程
        return checkToken(exchange, chain);
    }

    private Mono<Void> checkToken(ServerWebExchange exchange, WebFilterChain chain) {
        return tokenExtractionService.getAuthentication(exchange.getRequest())
                .flatMap(authentication -> {
                    // 根据 principal 类型获取对应的请求头名称并添加身份信息
                    ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();

                    Object principal = authentication.getPrincipal();
                    if (principal instanceof ClientPrincipal) {
                        // ClientPrincipal 使用独立的请求头
                        requestBuilder.header(CustomHeaderConstants.X_CLIENT_CONTEXT, JsonUtil.toJsonString(principal));
                    } else if (principal instanceof LoginUser) {
                        // LoginUser 使用原有的请求头
                        requestBuilder.header(CustomHeaderConstants.X_USER_CONTEXT, JsonUtil.toJsonString(principal));
                    }

                    // 提取权限信息并透传到请求头
                    if (CollectionUtils.isNotEmpty(authentication.getAuthorities())) {
                        Set<String> permissions = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
                        requestBuilder.header(CustomHeaderConstants.X_PERMISSIONS, JsonUtil.toJsonString(permissions));
                    }

                    ServerWebExchange mutatedExchange = exchange.mutate()
                            .request(requestBuilder.build())
                            .build();

                    // 将 Authentication 设置到 SecurityContext 并传播到响应式链
                    return chain.filter(mutatedExchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                })
                .onErrorResume(ResultException.class, resultException -> {
                    ServerHttpResponse exchangeResponse = exchange.getResponse();
                    exchangeResponse.setRawStatusCode(resultException.getHttpStatusCode());
                    return WebFluxUtil.writeResponse(exchangeResponse, resultException.getResult());
                })
                .onErrorResume(throwable -> {
                    ServerHttpResponse exchangeResponse = exchange.getResponse();
                    if (throwable instanceof ResponseStatusException statusException) {
                        exchangeResponse.setRawStatusCode(statusException.getStatusCode().value());
                    } else {
                        exchangeResponse.setRawStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    }
                    return WebFluxUtil.writeResponse(exchangeResponse, BizBaseCodeEnum.SYSTEM_ERROR);
                });
    }

    /**
     * 判断是否为白名单路径
     */
    private boolean isPermitAllPath(String path) {
        return isPathPermitted(permitAllProperties.getIgnores(), path)
                || isPathPermitted(permitAllAnnotations, path);
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
