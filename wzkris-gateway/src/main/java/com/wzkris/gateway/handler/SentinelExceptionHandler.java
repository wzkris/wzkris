package com.wzkris.gateway.handler;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : sentinel限流异常处理
 * @date : 2023/12/7 14:31
 */
@Configuration
public class SentinelExceptionHandler implements BlockRequestHandler {

    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable e) {

        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Result.resp(BizCode.BAD_REQUEST, "请求限流")));
    }
}
