package com.wzkris.gateway.service;

import com.wzkris.auth.feign.token.req.TokenReq;
import com.wzkris.auth.feign.token.resp.TokenResponse;
import com.wzkris.common.apikey.config.SignkeyProperties;
import com.wzkris.common.apikey.utils.RequestSignerUtil;
import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.constant.QueryParamConstants;
import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.domain.LoginAdmin;
import com.wzkris.common.core.model.domain.LoginCustomer;
import com.wzkris.common.core.model.domain.LoginTenant;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.openfeign.exception.RpcException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 用户信息提取工具类 - 从UnifiedAuthenticationFilter中抽取的公共方法
 * @date : 2025/01/29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenExtractionService {

    private static final ParameterizedTypeReference<TokenResponse<LoginAdmin>> userReference =
            new ParameterizedTypeReference<>() {
            };

    private static final ParameterizedTypeReference<TokenResponse<LoginTenant>> tenantReference =
            new ParameterizedTypeReference<>() {
            };

    private static final ParameterizedTypeReference<TokenResponse<LoginCustomer>> customerReference =
            new ParameterizedTypeReference<>() {
            };

    private final WebClient tokenWebClient;

    private final SignkeyProperties signkeyProperties;

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 获取当前请求的用户信息
     *
     * @param request 请求对象
     * @return 用户信息
     */
    public <T extends MyPrincipal> Mono<T> getCurrentPrincipal(ServerHttpRequest request) {
        if (!hasAnyToken(request)) {
            return Mono.error(new RpcException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Authorization token not found!!")));
        }

        String adminToken = getAdminToken(request);
        if (StringUtil.isNotBlank(adminToken)) {
            return (Mono<T>) validatePrincipal(AuthType.ADMIN.getValue(), adminToken, userReference);
        }

        String tenantToken = getTenantToken(request);
        if (StringUtil.isNotBlank(tenantToken)) {
            return (Mono<T>) validatePrincipal(AuthType.TENANT.getValue(), tenantToken, tenantReference);
        }

        String customerToken = getCustomerToken(request);
        if (StringUtil.isNotBlank(customerToken)) {
            return (Mono<T>) validatePrincipal(AuthType.CUSTOMER.getValue(), customerToken, customerReference);
        }

        // 理论上不会执行到这里，因为hasAnyToken已经检查过
        return Mono.error(new RpcException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Authorization token type not found!!")));
    }

    /**
     * 调用认证服务验证Token
     */
    private <T extends MyPrincipal> Mono<T> validatePrincipal(
            String authType,
            String token,
            ParameterizedTypeReference<TokenResponse<T>> typeReference) {
        TokenReq tokenReq = new TokenReq(authType, token);

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
                    return response.bodyToMono(String.class)
                            .doOnNext(responseStr -> {
                                log.error("Authentication called failed. Status: {}, Response body: {}",
                                        response.statusCode(), responseStr);
                            })
                            .map(responseStr -> {
                                try {
                                    return JsonUtil.parseObject(responseStr, Result.class);
                                } catch (Exception e) {
                                    log.error("Failed to parse error response body: {}", responseStr, e);
                                    return Result.init(BizBaseCode.AUTHENTICATION_ERROR.value(), null, "Unauthorized");
                                }
                            })
                            .flatMap(responseBody -> Mono.error(new RpcException(response.statusCode().value(), responseBody)));
                })
                .bodyToMono(typeReference)
                .flatMap(tokenResponse -> {
                    if (tokenResponse == null || !tokenResponse.isSuccess()) {
                        log.error("Token validation failed. {}", tokenResponse);
                        String errMsg = (tokenResponse != null) ? tokenResponse.getDescription() : "Token validation failed";
                        return Mono.error(new RpcException(HttpStatus.UNAUTHORIZED.value(), Result.unauth(errMsg)));
                    } else {
                        T principal = tokenResponse.getPrincipal();

                        return Mono.just(principal);
                    }
                });
    }

    /**
     * 检查是否携带任一认证Token
     */
    private boolean hasAnyToken(ServerHttpRequest request) {
        return Stream.of(
                        request.getHeaders().getFirst(CustomHeaderConstants.X_ADMIN_TOKEN),
                        request.getHeaders().getFirst(CustomHeaderConstants.X_TENANT_TOKEN),
                        request.getHeaders().getFirst(CustomHeaderConstants.X_CUSTOMER_TOKEN),
                        request.getQueryParams().getFirst(QueryParamConstants.X_ADMIN_TOKEN),
                        request.getQueryParams().getFirst(QueryParamConstants.X_TENANT_TOKEN),
                        request.getQueryParams().getFirst(QueryParamConstants.X_CUSTOMER_TOKEN)
                )
                .anyMatch(StringUtil::isNotBlank);
    }

    /**
     * 获取管理员Token（优先Header，其次Query参数）
     */
    private String getAdminToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(CustomHeaderConstants.X_ADMIN_TOKEN);
        if (StringUtil.isNotBlank(token)) {
            return token;
        }
        return request.getQueryParams().getFirst(QueryParamConstants.X_ADMIN_TOKEN);
    }

    /**
     * 获取租户Token（优先Header，其次Query参数）
     */
    private String getTenantToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(CustomHeaderConstants.X_TENANT_TOKEN);
        if (StringUtil.isNotBlank(token)) {
            return token;
        }
        return request.getQueryParams().getFirst(QueryParamConstants.X_TENANT_TOKEN);
    }

    /**
     * 获取客户Token（优先Header，其次Query参数）
     */
    private String getCustomerToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(CustomHeaderConstants.X_CUSTOMER_TOKEN);
        if (StringUtil.isNotBlank(token)) {
            return token;
        }
        return request.getQueryParams().getFirst(QueryParamConstants.X_CUSTOMER_TOKEN);
    }

}
