package com.wzkris.gateway.filter.web;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.exception.service.ResultException;
import com.wzkris.common.core.model.UserPrincipal;
import com.wzkris.common.core.model.domain.LoginAdmin;
import com.wzkris.common.core.model.domain.LoginClient;
import com.wzkris.common.core.model.domain.LoginCustomer;
import com.wzkris.common.core.model.domain.LoginTenant;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.gateway.properties.PermitAllProperties;
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
import org.springframework.http.server.reactive.ServerHttpRequest;
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

    public static Optional<UserPrincipal> getPrincipal(ContextView contextView) {
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
                .flatMap(principal -> {
                    // 根据 principal 类型获取对应的请求头名称并添加身份信息
                    ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();

                    requestBuilder.header(getInfoHeader(principal), JsonUtil.toJsonString(principal));

                    ServerWebExchange mutatedExchange = exchange.mutate()
                            .request(requestBuilder.build())
                            .principal(Mono.just(principal))
                            .build();
                    return chain.filter(mutatedExchange)
                            .contextWrite(context -> context.put(GATEWAY_PRINCIPAL, principal));
                })
                .onErrorResume(ResultException.class, resultException -> {
                    ServerHttpResponse exchangeResponse = exchange.getResponse();
                    exchangeResponse.setRawStatusCode(resultException.getHttpStatusCode());
                    return WebFluxUtil.writeResponse(exchangeResponse, resultException.getResult());
                })
                .onErrorResume(throwable -> {
                    ServerHttpResponse exchangeResponse = exchange.getResponse();
                    exchangeResponse.setRawStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return WebFluxUtil.writeResponse(exchangeResponse, BizBaseCodeEnum.SYSTEM_ERROR);
                });
    }

    /**
     * 根据 principal 类型获取对应的请求头名称
     *
     * @param principal 用户主体
     * @return 请求头名称，如果类型不匹配则返回 null
     */
    private String getInfoHeader(UserPrincipal principal) {
        if (principal instanceof LoginAdmin) {
            return CustomHeaderConstants.X_ADMIN_INFO;
        } else if (principal instanceof LoginTenant) {
            return CustomHeaderConstants.X_TENANT_INFO;
        } else if (principal instanceof LoginCustomer) {
            return CustomHeaderConstants.X_CUSTOMER_INFO;
        } else if (principal instanceof LoginClient) {
            return CustomHeaderConstants.X_CLIENT_INFO;
        }
        return null;
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
