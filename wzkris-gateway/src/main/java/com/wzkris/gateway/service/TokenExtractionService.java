package com.wzkris.gateway.service;

import com.wzkris.auth.httpservice.token.TokenHttpService;
import com.wzkris.auth.httpservice.token.req.TokenReq;
import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.constant.QueryParamConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.exception.service.ResultException;
import com.wzkris.common.core.model.UserPrincipal;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.domain.LoginAdmin;
import com.wzkris.common.core.model.domain.LoginClient;
import com.wzkris.common.core.model.domain.LoginCustomer;
import com.wzkris.common.core.model.domain.LoginTenant;
import com.wzkris.common.core.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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

    private final TokenHttpService tokenHttpService;

    /**
     * 获取当前请求的用户信息
     *
     * @param request 请求对象
     * @return 用户信息
     */
    public Mono<? extends UserPrincipal> getCurrentPrincipal(ServerHttpRequest request) {
        if (!hasAnyToken(request)) {
            return Mono.error(new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Authorization token not found!!")));
        }

        String adminToken = getAdminToken(request);
        if (StringUtil.isNotBlank(adminToken)) {
            return validatePrincipal(AuthTypeEnum.ADMIN.getValue(), adminToken, LoginAdmin.class);
        }

        String tenantToken = getTenantToken(request);
        if (StringUtil.isNotBlank(tenantToken)) {
            return validatePrincipal(AuthTypeEnum.TENANT.getValue(), tenantToken, LoginTenant.class);
        }

        String customerToken = getCustomerToken(request);
        if (StringUtil.isNotBlank(customerToken)) {
            return validatePrincipal(AuthTypeEnum.CUSTOMER.getValue(), customerToken, LoginCustomer.class);
        }

        String clientToken = getClientToken(request);
        if (StringUtil.isNotBlank(clientToken)) {
            return validatePrincipal(AuthTypeEnum.CLIENT.getValue(), clientToken, LoginClient.class);
        }

        // 理论上不会执行到这里，因为hasAnyToken已经检查过
        return Mono.error(new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Authorization token type not found!!")));
    }

    /**
     * 调用认证服务验证Token
     */
    private <T extends UserPrincipal> Mono<T> validatePrincipal(
            String authType,
            String token,
            Class<T> targetType) {
        TokenReq tokenReq = new TokenReq(authType, token);

        return Mono.fromCallable(() -> tokenHttpService.introspect(tokenReq))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(tokenResponse -> {
                    if (tokenResponse == null || !tokenResponse.isSuccess()) {
                        log.error("Token validation failed. {}", tokenResponse);
                        String errMsg = (tokenResponse != null) ? tokenResponse.getDescription() : "Token validation failed";
                        return Mono.error(new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth(errMsg)));
                    } else {
                        UserPrincipal principal = tokenResponse.getPrincipal();
                        if (principal == null) {
                            return Mono.error(new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Principal not found")));
                        }
                        if (!targetType.isInstance(principal)) {
                            log.error("Principal type mismatch. expected: {}, actual: {}", targetType.getSimpleName(), principal.getClass().getSimpleName());
                            return Mono.error(new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Invalid principal type")));
                        }
                        return Mono.just(targetType.cast(principal));
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
                        request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION),
                        request.getQueryParams().getFirst(QueryParamConstants.X_ADMIN_TOKEN),
                        request.getQueryParams().getFirst(QueryParamConstants.X_TENANT_TOKEN),
                        request.getQueryParams().getFirst(QueryParamConstants.X_CUSTOMER_TOKEN),
                        request.getQueryParams().getFirst(HttpHeaders.AUTHORIZATION)
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

    private String getClientToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtil.isNotBlank(token)) {
            return token;
        }
        return request.getQueryParams().getFirst(HttpHeaders.AUTHORIZATION);
    }

}
