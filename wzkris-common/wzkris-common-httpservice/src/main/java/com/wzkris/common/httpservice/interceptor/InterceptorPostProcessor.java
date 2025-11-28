package com.wzkris.common.httpservice.interceptor;

import org.springframework.core.Ordered;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * 抽象的拦截器后置处理器
 */
public interface InterceptorPostProcessor extends Ordered {

    default void postHandleBeforeRequest(HttpRequest request, byte[] body) {
    }

    default void postHandleAfterResponse(HttpRequest request, byte[] body, ClientHttpResponse response) throws IOException {
    }

}
