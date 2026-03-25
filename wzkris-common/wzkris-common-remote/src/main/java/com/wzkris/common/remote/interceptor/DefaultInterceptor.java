package com.wzkris.common.remote.interceptor;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.utils.TraceIdUtil;
import com.wzkris.common.remote.event.RemoteCallEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class DefaultInterceptor implements ClientHttpRequestInterceptor, Ordered {

    private final ApplicationEventPublisher publisher;

    public DefaultInterceptor(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        buildRequestHeaders(request);

        ClientHttpResponse response = execution.execute(request, body);

        doAfterResponse(request, body, response);
        return response;
    }

    private void buildRequestHeaders(HttpRequest request) {
        if (!request.getHeaders().containsKey(CustomHeaderConstants.X_REQUEST_TIME)) {
            request.getHeaders().add(CustomHeaderConstants.X_REQUEST_TIME, String.valueOf(System.currentTimeMillis()));
        }
        if (!request.getHeaders().containsKey(CustomHeaderConstants.X_TRACING_ID)) {
            request.getHeaders().add(CustomHeaderConstants.X_TRACING_ID, TraceIdUtil.getOrGenerate());
        }
    }

    private void doAfterResponse(HttpRequest request, byte[] body, ClientHttpResponse response) throws IOException {
        final RemoteCallEvent event = new RemoteCallEvent();
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
        long costTime = first == null ? -1L : System.currentTimeMillis() - Long.parseLong(first);
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
