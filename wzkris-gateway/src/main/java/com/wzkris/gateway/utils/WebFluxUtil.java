package com.wzkris.gateway.utils;

import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.JsonUtil;
import jakarta.annotation.Nullable;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 响应式工具
 * @date : 2023/9/8 13:31
 */
public class WebFluxUtil {

    public static Mono<Void> writeResponse(ServerHttpResponse response, BizBaseCode bizBaseCode) {
        return writeResponse(response, Result.resp(bizBaseCode.value(), null, bizBaseCode.desc()));
    }

    public static Mono<Void> writeResponse(ServerHttpResponse response, BizBaseCode bizBaseCode, String msg) {
        return writeResponse(response, Result.resp(bizBaseCode, msg));
    }

    public static Mono<Void> writeResponse(ServerHttpResponse response, int biz, String msg) {
        return writeResponse(response, Result.resp(biz, null, msg));
    }

    /**
     * 设置webflux模型响应
     *
     * @param response 响应
     * @param obj      响应体
     * @return Mono<Void>
     */
    public static Mono<Void> writeResponse(ServerHttpResponse response, @Nullable Object obj) {
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        byte[] info = JsonUtil.toBytes(obj);
        DataBuffer dataBuffer = response.bufferFactory().wrap(info);
        return response.writeWith(Mono.just(dataBuffer));
    }

}
