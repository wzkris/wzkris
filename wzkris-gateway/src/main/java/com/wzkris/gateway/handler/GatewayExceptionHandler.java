package com.wzkris.gateway.handler;

import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.gateway.utils.WebFluxUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * 网关统一异常处理
 *
 * @author wzkris
 */
@Slf4j
@Order(-1)
@Configuration
public class GatewayExceptionHandler implements WebExceptionHandler {

    /**
     * 异常处理
     *
     * @param exchange the current exchange
     * @param e        the exception to handle
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable e) {
        ServerHttpResponse response = exchange.getResponse();

        if (exchange.getResponse().isCommitted()) {
            return Mono.error(e);
        }
        log.error("[网关异常处理]请求路径:'{} {}',异常信息:{}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getPath(),
                e.getMessage(), e);

        return WebFluxUtil.writeResponse(response, BizBaseCode.BAD_GATEWAY.value(), e.getMessage());
    }

}
