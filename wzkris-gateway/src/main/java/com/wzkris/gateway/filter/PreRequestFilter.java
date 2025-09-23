package com.wzkris.gateway.filter;

import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.constant.QueryParamConstants;
import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.core.utils.TraceIdUtil;
import com.wzkris.gateway.config.PermitAllProperties;
import com.wzkris.gateway.utils.WebFluxUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 请求前拦截器，转发微服务前处理请求
 * @date : 2023/4/13 15:22
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class PreRequestFilter implements GlobalFilter {

    static final AntPathMatcher matcher = new AntPathMatcher();

    @Autowired
    private PermitAllProperties permitAllProperties;

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

    /**
     * do something
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();

        // 检查是否是黑名单
        if (isPathDenied(request.getPath().value())) {
            return WebFluxUtil.writeResponse(exchange.getResponse(), BizBaseCode.FORBID);
        }

        // 检查是否需要认证
        if (isAuthenticationRequired(request)) {
            return WebFluxUtil.writeResponse(exchange.getResponse(), BizBaseCode.UNAUTHORIZED);
        }

        // 添加追踪ID并继续过滤器链
        return chain.filter(
                exchange.mutate()
                        .request(
                                request.mutate()
                                        .header(HeaderConstants.X_TRACING_ID, TraceIdUtil.get())
                                        .build()
                        )
                        .build()
        );
    }

    private boolean isAuthenticationRequired(ServerHttpRequest request) {
        // 检查是否有任一认证token存在
        boolean hasToken = Stream.of(
                        request.getHeaders().getFirst(HeaderConstants.X_CUSTOMER_TOKEN),
                        request.getHeaders().getFirst(HeaderConstants.X_User_TOKEN),
                        request.getQueryParams().getFirst(QueryParamConstants.X_CUSTOMER_TOKEN),
                        request.getQueryParams().getFirst(QueryParamConstants.X_User_TOKEN)
                )
                .anyMatch(StringUtil::isNotBlank);

        // 如果没有任何token且路径不在白名单中，则需要认证
        return !hasToken && !isPathPermitted(request.getPath().value());
    }

    private boolean isPathPermitted(String url) {
        for (String path : permitAllProperties.getIgnores()) {
            if (matcher.match(path, url)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPathDenied(String url) {
        for (String path : permitAllProperties.getDenys()) {
            if (matcher.match(path, url)) {
                return true;
            }
        }
        return false;
    }

}
