package com.wzkris.gateway.filter.route;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.model.domain.LoginAdmin;
import com.wzkris.common.core.model.domain.LoginClient;
import com.wzkris.common.core.model.domain.LoginCustomer;
import com.wzkris.common.core.model.domain.LoginTenant;
import com.wzkris.common.core.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();

        return exchange.getPrincipal()
                .flatMap(principal -> {
                    String infoHeader;
                    if (principal instanceof LoginAdmin) {
                        infoHeader = CustomHeaderConstants.X_ADMIN_INFO;
                    } else if (principal instanceof LoginTenant) {
                        infoHeader = CustomHeaderConstants.X_TENANT_INFO;
                    } else if (principal instanceof LoginCustomer) {
                        infoHeader = CustomHeaderConstants.X_CUSTOMER_INFO;
                    } else if (principal instanceof LoginClient) {
                        infoHeader = CustomHeaderConstants.X_CLIENT_INFO;
                    } else {
                        return Mono.empty();
                    }

                    // 追加身份信息头
                    requestBuilder.header(infoHeader, JsonUtil.toJsonString(principal));

                    return Mono.just(principal);
                })
                .then(Mono.defer(() -> {
                    ServerWebExchange newExchange = exchange.mutate().request(requestBuilder.build()).build();

                    return chain.filter(newExchange);
                }));
    }

}
