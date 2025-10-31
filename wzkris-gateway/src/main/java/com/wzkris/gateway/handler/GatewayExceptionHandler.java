package com.wzkris.gateway.handler;

import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.gateway.utils.WebFluxUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.handler.WebFluxResponseStatusExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关统一异常处理
 *
 * @author wzkris
 */
@Slf4j
@Order(-1)
@Configuration
public class GatewayExceptionHandler extends WebFluxResponseStatusExceptionHandler {

    /**
     * 异常处理
     *
     * @param exchange the current exchange
     * @param ex       the exception to handle
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }

        log.error("[网关异常处理]请求路径:'{} {}',异常信息:{}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getPath(),
                ex.getMessage(), ex);

        HttpStatusCode httpStatusCode = determineStatus(ex);
        response.setStatusCode(httpStatusCode);
        return WebFluxUtil.writeResponse(response, BizBaseCode.SYSTEM_ERROR.value(), ex.getMessage());
    }

}
