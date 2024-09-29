package com.wzkris.gateway.handler;

import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.base.BaseException;
import com.wzkris.gateway.utils.WebFluxUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
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
        log.error("[网关异常处理]请求路径:'{} {}',异常信息:{}", exchange.getRequest().getMethod(), exchange.getRequest().getPath(), e.getMessage());
        if (e instanceof ResponseStatusException respEx) {
            return switch (respEx.getStatusCode().value()) {
                case 400 -> WebFluxUtil.writeResponse(response, BizCode.BAD_REQUEST);
                case 401 -> WebFluxUtil.writeResponse(response, BizCode.UNAUTHORIZED);
                case 403 -> WebFluxUtil.writeResponse(response, BizCode.FORBID);
                case 404 -> WebFluxUtil.writeResponse(response, BizCode.NOT_FOUND);
                case 405 -> WebFluxUtil.writeResponse(response, BizCode.BAD_METHOD);
                case 500 -> WebFluxUtil.writeResponse(response, BizCode.INTERNAL_ERROR);
                case 502 -> WebFluxUtil.writeResponse(response, BizCode.BAD_GATEWAY);
                case 503 -> WebFluxUtil.writeResponse(response, BizCode.SERVICE_UNAVAILABLE);
                default -> WebFluxUtil.writeResponse(response, respEx.getStatusCode().value(), respEx.getMessage());
            };
        }
        else if (e instanceof BaseException bizEx) {
            // 若状态码为远程调用异常，则返回前端数据需要覆盖
            String errorMsg = bizEx.getMessage();
            if (bizEx.getBiz() == BizCode.RPC_INVOCATION.value()) {
                errorMsg = BizCode.RPC_INVOCATION.desc();
            }
            return WebFluxUtil.writeResponse(response, bizEx.getBiz(), errorMsg);
        }

        // 返回500内部异常
        return WebFluxUtil.writeResponse(response, BizCode.INTERNAL_ERROR, e.getMessage());
    }

}
