package com.wzkris.gateway.filter;

import com.wzkris.auth.api.RemoteTokenApi;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.utils.I18nUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.gateway.config.PermitAllProperties;
import com.wzkris.gateway.utils.WebFluxUtil;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 请求前拦截器，转发微服务前处理请求
 * @date : 2023/4/13 15:22
 */
@Slf4j
@Component
public class PreRequestFilter implements GlobalFilter, Ordered {

    @Autowired
    private PermitAllProperties permitAllProperties;

    @Lazy
    @Autowired
    private RemoteTokenApi remoteTokenApi;

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

    private static final ExecutorService executorService = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors() * 3,
            Runtime.getRuntime().availableProcessors() * 3 * 2,
            10L,
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new DefaultThreadFactory("token-requestid-pool", true, Thread.NORM_PRIORITY, new ThreadGroup("token_request_id")),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    /**
     * do something
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();
        final ServerHttpRequest.Builder mutate = request.mutate();

        if (StringUtil.matches(request.getURI().getPath(), permitAllProperties.getIgnores())) {
            return chain.filter(exchange);
        }

        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtil.isBlank(bearerToken)) {
            List<String> list = request.getQueryParams().get(HttpHeaders.AUTHORIZATION);
            if (list == null || list.isEmpty()) {
                return WebFluxUtil.writeResponse(exchange.getResponse(), BizCode.UNAUTHORIZED);
            }
            bearerToken = list.get(0);
        }

        final String token = bearerToken.replaceFirst(SecurityConstants.TOKEN_PREFIX, "");

        Future<Result<String>> future = executorService.submit(() -> {
            return remoteTokenApi.getTokenReqId(token);
        });

        try {
            Result<String> result = future.get();

            if (!result.isSuccess()) {
                return WebFluxUtil.writeResponse(exchange.getResponse(), result.getCode(), I18nUtil.message("service.busy"));
            }

            mutate.header(SecurityConstants.TOKEN_REQ_ID_HEADER, result.getData());
        }
        catch (InterruptedException | ExecutionException e) {
            log.error("token请求ID获取异常，errmsg：{}", e.getMessage());
            return WebFluxUtil.writeResponse(exchange.getResponse(), BizCode.INTERNAL_ERROR);
        }

        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
