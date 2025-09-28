package com.wzkris.gateway.filter;

import com.wzkris.common.apikey.config.SignkeyProperties;
import com.wzkris.common.apikey.utils.RequestSignerUtil;
import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.utils.TraceIdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR;

/**
 * 请求头追加
 *
 * @author wzkris
 */
@Slf4j
@Order(100)
@Component
@RequiredArgsConstructor
public class AppendHeaderFilter implements GlobalFilter {

    private final SignkeyProperties signkeyProperties;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long l = System.currentTimeMillis();
        ServerHttpRequest httpRequest = exchange.getRequest().mutate()
                .headers(header -> {
                    header.add(HeaderConstants.X_TRACING_ID, TraceIdUtil.get());// tracingID
                    header.add(HeaderConstants.X_REQUEST_TIME, String.valueOf(l));// 请求时间 ->防重放
                    header.add(HeaderConstants.X_REQUEST_FROM, applicationName);// 请求来源服务
                    String reqBodyStr;
                    Object bodyAttr = exchange.getAttribute(CACHED_REQUEST_BODY_ATTR);
                    if (bodyAttr instanceof DataBuffer dataBuffer) {
                        reqBodyStr = dataBuffer.toString(StandardCharsets.UTF_8);
                    } else {
                        reqBodyStr = (String) bodyAttr;
                    }
                    header.add(HeaderConstants.X_REQUEST_SIGN, RequestSignerUtil.generateSignature(
                            signkeyProperties.getKeys().get(applicationName).getSecret(),
                            exchange.getRequest().getPath().value(),
                            reqBodyStr,
                            l
                    ));// 请求签名 -> 防止伪造请求
                }).build();
        return chain.filter(exchange.mutate().request(httpRequest).build());
    }

}
