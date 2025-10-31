package com.wzkris.common.openfeign.handler;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.openfeign.exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 远程异常处理器
 *
 * @author wzkris
 */
@Slf4j
@Order(-1)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@RestControllerAdvice
public class RpcExceptionWebfluxHandler {

    @ExceptionHandler(RpcException.class)
    public Mono<ResponseEntity<Result>> handleRpcException(RpcException e, ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        log.error("请求地址'{} {}',发生远程调用异常，异常信息：{}", request.getMethod(), request.getPath().value(), e.getMessage());

        return Mono.just(ResponseEntity.status(e.getHttpStatusCode())
                .body(e.getResult()));
    }

}
