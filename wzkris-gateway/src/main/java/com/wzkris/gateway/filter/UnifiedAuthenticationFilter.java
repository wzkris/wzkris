package com.wzkris.gateway.filter;

import com.wzkris.auth.feign.token.req.TokenReq;
import com.wzkris.auth.feign.token.resp.TokenResponse;
import com.wzkris.common.apikey.config.SignkeyProperties;
import com.wzkris.common.apikey.utils.RequestSignerUtil;
import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.constant.QueryParamConstants;
import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.domain.LoginCustomer;
import com.wzkris.common.core.model.domain.LoginStaff;
import com.wzkris.common.core.model.domain.LoginUser;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.openfeign.exception.RpcException;
import com.wzkris.gateway.config.PermitAllProperties;
import com.wzkris.gateway.utils.WebFluxUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
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

    static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    static final ParameterizedTypeReference<TokenResponse<LoginUser>> userReference = new ParameterizedTypeReference<>() {
    };

    static final ParameterizedTypeReference<TokenResponse<LoginStaff>> staffReference = new ParameterizedTypeReference<>() {
    };

    static final ParameterizedTypeReference<TokenResponse<LoginCustomer>> customerReference = new ParameterizedTypeReference<>() {
    };

    private final WebClient tokenWebClient;

    private final PermitAllProperties permitAllProperties;

    private final SignkeyProperties signkeyProperties;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();
        final String path = request.getPath().value();

        // 1. 黑名单拦截
        if (isPathDenied(path)) {
            log.error("Access denied to blacklisted path: {}", path);
            return WebFluxUtil.writeResponse(exchange.getResponse(), BizBaseCode.FORBID);
        }

        // 2. 白名单直接放行
        if (isPathPermitted(path)) {
            return chain.filter(exchange);
        }

        // 3. 检查是否携带Token
        if (!hasAnyToken(request)) {
            log.error("No authentication token found for path: {}", path);
            return WebFluxUtil.writeResponse(exchange.getResponse(), BizBaseCode.UNAUTHORIZED);
        }

        // 4. Token认证流程
        return processTokenAuthentication(exchange, chain, request);
    }

    /**
     * 处理Token认证流程
     */
    private Mono<Void> processTokenAuthentication(
            ServerWebExchange exchange,
            GatewayFilterChain chain,
            ServerHttpRequest request) {
        String userToken = getUserToken(request);
        if (StringUtil.isNotBlank(userToken)) {
            return authenticateAndProceed(exchange, chain, userToken, HeaderConstants.X_USER_INFO, userReference);
        }

        String staffToken = getStaffToken(request);
        if (StringUtil.isNotBlank(staffToken)) {
            return authenticateAndProceed(exchange, chain, staffToken, HeaderConstants.X_STAFF_INFO, staffReference);
        }

        String customerToken = getCustomerToken(request);
        if (StringUtil.isNotBlank(customerToken)) {
            return authenticateAndProceed(exchange, chain, customerToken, HeaderConstants.X_CUSTOMER_INFO, customerReference);
        }

        // 理论上不会执行到这里，因为hasAnyToken已经检查过
        return WebFluxUtil.writeResponse(exchange.getResponse(), BizBaseCode.UNAUTHORIZED);
    }

    /**
     * 执行Token认证并继续过滤器链
     */
    private <T extends MyPrincipal> Mono<Void> authenticateAndProceed(
            ServerWebExchange exchange,
            GatewayFilterChain chain,
            String token,
            String infoHeader,
            ParameterizedTypeReference<TokenResponse<T>> typeReference) {
        return validatePrincipal(token, typeReference)
                .flatMap(tokenResponse -> {
                    if (tokenResponse != null && tokenResponse.isSuccess()) {
                        MyPrincipal principal = tokenResponse.getPrincipal();

                        // 添加用户信息和Token类型到请求头
                        ServerHttpRequest newRequest = exchange.getRequest().mutate()
                                .header(infoHeader, JsonUtil.toJsonString(principal))
                                .build();

                        return chain.filter(exchange.mutate().request(newRequest).build());
                    } else {
                        return WebFluxUtil.writeResponse(exchange.getResponse(), BizBaseCode.UNAUTHORIZED);
                    }
                })
                .onErrorResume(RpcException.class, rpcException -> {
                    log.error("Auth service call failed for token: {}", token, rpcException);
                    return WebFluxUtil.writeResponse(exchange.getResponse(), rpcException.getResult());
                })
                .onErrorResume(throwable -> {
                    log.error("Auth service call failed for token: {}", token, throwable);
                    return WebFluxUtil.writeResponse(exchange.getResponse(), BizBaseCode.INTERNAL_ERROR);
                });
    }

    /**
     * 调用认证服务验证Token
     */
    private <T extends MyPrincipal> Mono<TokenResponse<T>> validatePrincipal(
            String token,
            ParameterizedTypeReference<TokenResponse<T>> typeReference) {
        TokenReq tokenReq = new TokenReq(token);

        return tokenWebClient.post()
                .uri("/feign-token/check-principal")
                .bodyValue(tokenReq)
                .headers(httpHeaders -> {
                    RequestSignerUtil.setCommonHeaders(httpHeaders::add,
                            applicationName,
                            signkeyProperties.getKeys().get(applicationName).getSecret(),
                            JsonUtil.toJsonString(tokenReq),
                            System.currentTimeMillis()
                    );// 请求签名 -> 防止伪造请求
                })
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    // 处理错误状态码，获取响应体
                    return response.bodyToMono(Result.class)
                            .flatMap(responseBody -> {
                                log.error("Token validation failed. Status: {}, Response body: {}",
                                        response.statusCode(), responseBody);
                                return Mono.error(new RpcException(response.statusCode().value(), responseBody));
                            });
                })
                .bodyToMono(typeReference);
    }

    /**
     * 检查是否携带任一认证Token
     */
    private boolean hasAnyToken(ServerHttpRequest request) {
        return Stream.of(
                        request.getHeaders().getFirst(HeaderConstants.X_USER_TOKEN),
                        request.getHeaders().getFirst(HeaderConstants.X_STAFF_TOKEN),
                        request.getHeaders().getFirst(HeaderConstants.X_CUSTOMER_TOKEN),
                        request.getQueryParams().getFirst(QueryParamConstants.X_USER_TOKEN),
                        request.getQueryParams().getFirst(QueryParamConstants.X_STAFF_TOKEN),
                        request.getQueryParams().getFirst(QueryParamConstants.X_CUSTOMER_TOKEN)
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

    private String getStaffToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(HeaderConstants.X_STAFF_TOKEN);
        if (StringUtil.isNotBlank(token)) {
            return token;
        }
        return request.getQueryParams().getFirst(QueryParamConstants.X_STAFF_TOKEN);
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