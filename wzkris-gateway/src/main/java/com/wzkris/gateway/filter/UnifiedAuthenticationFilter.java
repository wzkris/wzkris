package com.wzkris.gateway.filter;

import com.wzkris.auth.feign.token.req.TokenReq;
import com.wzkris.auth.feign.token.resp.TokenResponse;
import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.constant.QueryParamConstants;
import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.core.utils.TraceIdUtil;
import com.wzkris.gateway.config.PermitAllProperties;
import com.wzkris.gateway.utils.WebFluxUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 统一认证过滤器（合并黑名单、白名单、Token认证逻辑）
 * @date : 2025/9/23
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@RequiredArgsConstructor
public class UnifiedAuthenticationFilter implements GlobalFilter {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final WebClient tokenWebClient;

    private final PermitAllProperties permitAllProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();
        final String path = request.getPath().value();

        // 1. 黑名单拦截（最高优先级）
        if (isPathDenied(path)) {
            log.warn("Access denied to blacklisted path: {}", path);
            return WebFluxUtil.writeResponse(exchange.getResponse(), BizBaseCode.FORBID);
        }

        // 2. 白名单直接放行（添加traceID后直接放行）
        if (isPathPermitted(path)) {
            log.debug("Path permitted, skipping authentication: {}", path);
            return chain.filter(exchange.mutate().request(addTracingHeader(exchange).build()).build());
        }

        // 3. 检查是否携带Token
        if (!hasAnyToken(request)) {
            log.warn("No authentication token found for path: {}", path);
            return WebFluxUtil.writeResponse(exchange.getResponse(), BizBaseCode.UNAUTHORIZED);
        }

        // 4. Token认证流程
        return processTokenAuthentication(exchange, chain, request);
    }

    /**
     * 处理Token认证流程
     */
    private Mono<Void> processTokenAuthentication(ServerWebExchange exchange,
                                                  GatewayFilterChain chain,
                                                  ServerHttpRequest request) {
        String userToken = getUserToken(request);
        if (StringUtil.isNotBlank(userToken)) {
            return authenticateAndProceed(exchange, chain, userToken, HeaderConstants.X_USER_INFO);
        }

        String customerToken = getCustomerToken(request);
        if (StringUtil.isNotBlank(customerToken)) {
            return authenticateAndProceed(exchange, chain, customerToken, HeaderConstants.X_CUSTOMER_INFO);
        }

        // 理论上不会执行到这里，因为hasAnyToken已经检查过
        return WebFluxUtil.writeResponse(exchange.getResponse(), BizBaseCode.UNAUTHORIZED);
    }

    /**
     * 执行Token认证并继续过滤器链
     */
    private Mono<Void> authenticateAndProceed(ServerWebExchange exchange,
                                              GatewayFilterChain chain,
                                              String token,
                                              String userInfoHeader) {
        return validateToken(token)
                .flatMap(tokenResponse -> {
                    if (tokenResponse != null && tokenResponse.isSuccess()) {
                        CorePrincipal principal = tokenResponse.getPrincipal();

                        // 添加用户信息和Token类型到请求头
                        ServerHttpRequest newRequest = addTracingHeader(exchange)
                                .header(userInfoHeader, JsonUtil.toJsonString(principal))
                                .build();

                        return chain.filter(exchange.mutate().request(newRequest).build());
                    } else {
                        log.error("token'{}' authentication failed: {}", token, tokenResponse);
                        return WebFluxUtil.writeResponse(exchange.getResponse(), BizBaseCode.UNAUTHORIZED);
                    }
                })
                .onErrorResume(throwable -> {
                    log.error("Authentication service call failed for token: {}", token, throwable);
                    return WebFluxUtil.writeResponse(exchange.getResponse(), BizBaseCode.INTERNAL_ERROR);
                });
    }

    /**
     * 调用认证服务验证Token
     */
    private Mono<TokenResponse> validateToken(String token) {
        TokenReq tokenReq = new TokenReq(token);

        return tokenWebClient.post()
                .uri("/feign-token/check-user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(tokenReq))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .doOnNext(response ->
                        log.debug("Token validation result: success={}", response.isSuccess()))
                .doOnError(error ->
                        log.error("Token validation error: {}", error.getMessage(), error));
    }

    /**
     * 添加追踪ID头信息
     */
    private ServerHttpRequest.Builder addTracingHeader(ServerWebExchange exchange) {
        return exchange.getRequest().mutate()
                .header(HeaderConstants.X_TRACING_ID, TraceIdUtil.get());
    }

    /**
     * 检查是否携带任一认证Token
     */
    private boolean hasAnyToken(ServerHttpRequest request) {
        return Stream.of(
                        request.getHeaders().getFirst(HeaderConstants.X_CUSTOMER_TOKEN),
                        request.getHeaders().getFirst(HeaderConstants.X_USER_TOKEN),
                        request.getQueryParams().getFirst(QueryParamConstants.X_CUSTOMER_TOKEN),
                        request.getQueryParams().getFirst(QueryParamConstants.X_USER_TOKEN)
                )
                .anyMatch(StringUtil::isNotBlank);
    }

    /**
     * 获取用户Token（优先Header，其次Query参数）
     */
    private String getUserToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(HeaderConstants.X_USER_TOKEN);
        if (StringUtil.isNotBlank(token)) {
            return token;
        }
        return request.getQueryParams().getFirst(QueryParamConstants.X_USER_TOKEN);
    }

    /**
     * 获取客户Token（优先Header，其次Query参数）
     */
    private String getCustomerToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(HeaderConstants.X_CUSTOMER_TOKEN);
        if (StringUtil.isNotBlank(token)) {
            return token;
        }
        return request.getQueryParams().getFirst(QueryParamConstants.X_CUSTOMER_TOKEN);
    }

    /**
     * 检查路径是否在白名单中
     */
    private boolean isPathPermitted(String url) {
        return permitAllProperties.getIgnores().stream()
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, url));
    }

    /**
     * 检查路径是否在黑名单中
     */
    private boolean isPathDenied(String url) {
        return permitAllProperties.getDenys().stream()
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, url));
    }

}