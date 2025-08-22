package com.wzkris.gateway.utils;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.common.core.utils.JsonUtil;
import jakarta.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
        return writeResponse(response, HttpStatus.OK, Result.resp(bizBaseCode));
    }

    public static Mono<Void> writeResponse(ServerHttpResponse response, BizBaseCode bizBaseCode, String msg) {
        return writeResponse(response, HttpStatus.OK, Result.resp(bizBaseCode, msg));
    }

    public static Mono<Void> writeResponse(ServerHttpResponse response, int biz, String msg) {
        return writeResponse(response, HttpStatus.OK, Result.resp(biz, null, msg));
    }

    /**
     * 设置webflux模型响应
     *
     * @param response   响应
     * @param httpStatus HTTP状态码
     * @param obj        响应体
     * @return Mono<Void>
     */
    public static Mono<Void> writeResponse(ServerHttpResponse response, HttpStatus httpStatus, @Nullable Object obj) {
        response.setStatusCode(httpStatus);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        byte[] info =
                obj == null ? httpStatus.getReasonPhrase().getBytes(StandardCharsets.UTF_8) : JsonUtil.toBytes(obj);
        DataBuffer dataBuffer = response.bufferFactory().wrap(info);
        return response.writeWith(Mono.just(dataBuffer));
    }
}
