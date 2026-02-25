package com.wzkris.common.httpclient.interceptor;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.httpclient.event.HttpClientCallEvent;
import com.wzkris.common.httpclient.interceptor.core.InterceptorPostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 发布事件和打印日志
 */
@Slf4j
public class PublishEventInterceptorPostProcessor implements InterceptorPostProcessor {

    private final ApplicationEventPublisher publisher;

    public PublishEventInterceptorPostProcessor(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void postHandleBeforeRequest(HttpRequest request, byte[] body) {
        if (!request.getHeaders().containsKey(CustomHeaderConstants.X_REQUEST_TIME)) {
            request.getHeaders().add(CustomHeaderConstants.X_REQUEST_TIME, String.valueOf(System.currentTimeMillis()));
        }
    }

    @Override
    public void postHandleAfterResponse(HttpRequest request, byte[] body, ClientHttpResponse response) throws IOException {
        final HttpClientCallEvent event = new HttpClientCallEvent();
        int httpStatusCode = response.getStatusCode().value();
        event.setHttpStatusCode(httpStatusCode);
        Map<String, String> requestHeaders = request.getHeaders().asSingleValueMap();
        event.setRequestHeaders(requestHeaders);
        String requestBody = new String(body, StandardCharsets.UTF_8);
        event.setRequestBody(requestBody);
        Map<String, String> responseHeaders = response.getHeaders().asSingleValueMap();
        event.setResponseHeaders(responseHeaders);
        String responseBody = new String(StreamUtils.copyToByteArray(response.getBody()), StandardCharsets.UTF_8);
        event.setResponseBody(responseBody);

        String first = request.getHeaders().getFirst(CustomHeaderConstants.X_REQUEST_TIME);
        long costTime = System.currentTimeMillis() - Long.parseLong(first);
        event.setCostTime(costTime);

        log.info("""
                        Http Service Client call =>
                        request url: {},
                        request headers: {},
                        request body: {},
                        response status: {},
                        response headers: {},
                        response body: {},
                        cost: {} ms
                        """,
                request.getURI(), requestHeaders, requestBody,
                httpStatusCode, responseHeaders, responseBody, costTime);

        publisher.publishEvent(event);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
