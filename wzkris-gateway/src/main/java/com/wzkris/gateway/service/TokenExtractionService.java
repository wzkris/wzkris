package com.wzkris.gateway.service;

import com.wzkris.auth.httpservice.token.LoginUserHttpService;
import com.wzkris.auth.httpservice.token.req.LoginUserReq;
import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.constant.QueryParamConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.exception.service.ResultException;
import com.wzkris.common.core.model.LoginUser;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Set;
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

    private final LoginUserHttpService loginUserHttpService;

    private final JwtDecoder jwtDecoder;

    /**
     * 获取当前请求的用户信息
     *
     * @param request 请求对象
     * @return 用户信息
     */
    public Mono<? extends Authentication> getCurrentPrincipal(ServerHttpRequest request) {
        if (!hasAnyToken(request)) {
            return Mono.error(new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Authorization token not found!!")));
        }

        String adminToken = getAdminToken(request);
        if (StringUtil.isNotBlank(adminToken)) {
            return validate(AuthTypeEnum.ADMIN, adminToken);
        }

        String tenantToken = getTenantToken(request);
        if (StringUtil.isNotBlank(tenantToken)) {
            return validate(AuthTypeEnum.TENANT, tenantToken);
        }

        String customerToken = getCustomerToken(request);
        if (StringUtil.isNotBlank(customerToken)) {
            return validate(AuthTypeEnum.CUSTOMER, customerToken);
        }

        String clientToken = getClientToken(request);
        if (StringUtil.isNotBlank(clientToken)) {
            return validate(AuthTypeEnum.CLIENT, clientToken);
        }

        // 理论上不会执行到这里，因为hasAnyToken已经检查过
        return Mono.error(new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Authorization token type not found!!")));
    }

    /**
     * 验证 JWT Token
     * 1. 本地验证 JWT（使用 Spring OAuth2 JwtDecoder）
     * 2. 解析出 uid
     * 3. 调用 auth 服务获取用户信息（通过 type 和 uid）
     */
    private Mono<Authentication> validate(AuthTypeEnum authTypeEnum, String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);

            String uidStr = jwt.getSubject();
            if (StringUtil.isBlank(uidStr)) {
                return Mono.error(new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Invalid token: missing subject")));
            }

            return introspect(authTypeEnum, Long.valueOf(uidStr), token);
        } catch (JwtException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            return Mono.error(new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Invalid token: " + e.getMessage())));
        } catch (Exception e) {
            log.error("Unexpected error during JWT validation", e);
            return Mono.error(new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Token validation error")));
        }
    }

    private Mono<Authentication> introspect(AuthTypeEnum authTypeEnum, Long uid, String token) {
        LoginUserReq loginUserReq = new LoginUserReq(authTypeEnum.getValue(), uid);
        return Mono.fromCallable(() -> loginUserHttpService.query(loginUserReq))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(tokenResponse -> {
                    if (tokenResponse == null || !tokenResponse.isSuccess()) {
                        log.warn("Token validation failed after JWT decode. {}", tokenResponse);
                        String errMsg = (tokenResponse != null) ? tokenResponse.getDescription() : "Token validation failed";
                        return Mono.error(new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth(errMsg)));
                    } else {
                        LoginUser loginUser = tokenResponse.getLoginUser();
                        if (loginUser == null) {
                            return Mono.error(new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Principal not found")));
                        }
                        // 从 TokenResponse 获取权限信息
                        Set<String> permissions = tokenResponse.getPermissions();
                        // 创建 Authentication 对象
                        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                                loginUser, token,
                                CollectionUtils.isNotEmpty(permissions)
                                        ? AuthorityUtils.createAuthorityList(permissions)
                                        : AuthorityUtils.NO_AUTHORITIES);
                        return Mono.just(authentication);
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
                        request.getHeaders().getFirst(CustomHeaderConstants.X_CLIENT_TOKEN),
                        request.getQueryParams().getFirst(QueryParamConstants.X_ADMIN_TOKEN),
                        request.getQueryParams().getFirst(QueryParamConstants.X_TENANT_TOKEN),
                        request.getQueryParams().getFirst(QueryParamConstants.X_CUSTOMER_TOKEN),
                        request.getQueryParams().getFirst(QueryParamConstants.X_CLIENT_TOKEN)
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
        String token = request.getHeaders().getFirst(CustomHeaderConstants.X_CLIENT_TOKEN);
        if (StringUtil.isNotBlank(token)) {
            return token;
        }
        return request.getQueryParams().getFirst(QueryParamConstants.X_CLIENT_TOKEN);
    }

}
