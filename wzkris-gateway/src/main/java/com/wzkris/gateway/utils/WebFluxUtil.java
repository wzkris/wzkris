package com.wzkris.gateway.utils;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.utils.json.JsonUtil;
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
    /**
     * 设置webflux模型响应
     *
     * @param response ServerHttpResponse
     * @param bizCode  服务码
     * @return Mono<Void>
     */
    public static Mono<Void> writeResponse(ServerHttpResponse response, BizCode bizCode) {
        return writeResponse(response, MediaType.APPLICATION_JSON_VALUE, bizCode.value(), bizCode.desc());
    }

    public static Mono<Void> writeResponse(ServerHttpResponse response, BizCode bizCode, String errMsg) {
        return writeResponse(response, MediaType.APPLICATION_JSON_VALUE, bizCode.value(), errMsg);
    }

    public static Mono<Void> writeResponse(ServerHttpResponse response, int biz, String errMsg) {
        return writeResponse(response, MediaType.APPLICATION_JSON_VALUE, biz, errMsg);
    }

    /**
     * 设置webflux模型响应
     *
     * @param response    ServerHttpResponse
     * @param contentType content-type
     * @return Mono<Void>
     */
    public static Mono<Void> writeResponse(ServerHttpResponse response, String contentType, int biz, String errMsg) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
        byte[] info = JsonUtil.toBytes(Result.resp(biz, null, errMsg));
        DataBuffer dataBuffer = response.bufferFactory().wrap(info);
        return response.writeWith(Mono.just(dataBuffer));
    }
}
