package com.wzkris.gateway.filter;

import com.wzkris.common.apikey.config.SignkeyProperties;
import com.wzkris.common.apikey.utils.RequestSignerUtil;
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
        ServerHttpRequest httpRequest = exchange.getRequest().mutate()
                .headers(header -> {
                    String reqBodyStr;
                    Object bodyAttr = exchange.getAttribute(CACHED_REQUEST_BODY_ATTR);
                    if (bodyAttr instanceof DataBuffer dataBuffer) {
                        reqBodyStr = dataBuffer.toString(StandardCharsets.UTF_8);
                    } else {
                        reqBodyStr = (String) bodyAttr;
                    }
                    RequestSignerUtil.setCommonHeaders(header::add,
                            applicationName,
                            signkeyProperties.getKeys().get(applicationName).getSecret(),
                            reqBodyStr,
                            System.currentTimeMillis()
                    );// 请求签名 -> 防止伪造请求
                }).build();
        return chain.filter(exchange.mutate().request(httpRequest).build());
    }

}
