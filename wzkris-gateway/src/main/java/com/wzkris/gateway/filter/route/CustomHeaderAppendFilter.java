package com.wzkris.gateway.filter.route;

import com.wzkris.common.apikey.config.SignkeyProperties;
import com.wzkris.common.apikey.utils.RequestSignerUtil;
import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.core.model.domain.LoginAdmin;
import com.wzkris.common.core.model.domain.LoginCustomer;
import com.wzkris.common.core.model.domain.LoginTenant;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
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
 * 自定义请求头追加
 *
 * @author wzkris
 */
@Slf4j
@Order(100)
@Component
@RequiredArgsConstructor
public class CustomHeaderAppendFilter implements GlobalFilter {

    private final SignkeyProperties signkeyProperties;

    private final LoadBalancerProperties loadBalancerProperties;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();

        return exchange.getPrincipal()
                .flatMap(principal -> {
                    String infoHeader;
                    if (principal instanceof LoginAdmin) {
                        infoHeader = CustomHeaderConstants.X_Admin_INFO;
                    } else if (principal instanceof LoginTenant) {
                        infoHeader = CustomHeaderConstants.X_TENANT_INFO;
                    } else if (principal instanceof LoginCustomer) {
                        infoHeader = CustomHeaderConstants.X_CUSTOMER_INFO;
                    } else {
                        return Mono.empty();
                    }

                    // 追加身份信息头
                    requestBuilder.header(infoHeader, JsonUtil.toJsonString(principal));

                    // 不为空且不为默认版本号则透传
                    String version = ((MyPrincipal) principal).getVersion();
                    if (StringUtil.isNotBlank(version) && !StringUtil.equals(version, SecurityConstants.DEFAULT_VERSION)) {
                        requestBuilder.header(loadBalancerProperties.getHintHeaderName(), version);
                    }
                    return Mono.just(principal);
                })
                .then(Mono.defer(() -> {
                    // 无论是否有身份信息，都继续处理请求签名
                    return appendSignatureHeaders(exchange, chain, requestBuilder);
                }));
    }

    /**
     * 追加请求签名头
     */
    private Mono<Void> appendSignatureHeaders(ServerWebExchange exchange,
                                              GatewayFilterChain chain,
                                              ServerHttpRequest.Builder requestBuilder) {
        // 追加请求签名头
        requestBuilder.headers(header -> {
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
        });

        ServerHttpRequest newRequest = requestBuilder.build();
        return chain.filter(exchange.mutate().request(newRequest).build());
    }

}
