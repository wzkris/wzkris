package com.wzkris.common.remote.interceptor;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.utils.TraceIdUtil;
import com.wzkris.common.remote.event.RemoteCallEvent;
import com.wzkris.common.remote.properties.RemoteInterfaceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class DefaultInterceptor implements RemoteClientRequestInterceptor, Ordered {

    private final ApplicationEventPublisher publisher;

    private final RemoteInterfaceProperties remoteInterfaceProperties;

    public DefaultInterceptor(
            ApplicationEventPublisher publisher,
            RemoteInterfaceProperties remoteInterfaceProperties) {
        this.publisher = publisher;
        this.remoteInterfaceProperties = remoteInterfaceProperties;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        buildRequestHeaders(request);

        ClientHttpResponse response = null;
        Throwable error = null;
        try {
            response = execution.execute(request, body);
            return response;
        } catch (IOException | RuntimeException | Error ex) {
            error = ex;
            throw ex;
        } finally {
            publishCallEventSafely(request, body, response, error);
        }
    }

    private void buildRequestHeaders(HttpRequest request) {
        if (!request.getHeaders().containsKey(CustomHeaderConstants.X_REQUEST_TIME)) {
            request.getHeaders().add(CustomHeaderConstants.X_REQUEST_TIME, String.valueOf(System.currentTimeMillis()));
        }
        if (!request.getHeaders().containsKey(CustomHeaderConstants.X_TRACING_ID)) {
            request.getHeaders().add(CustomHeaderConstants.X_TRACING_ID, TraceIdUtil.getOrGenerate());
        }
    }

    private void publishCallEventSafely(
            HttpRequest request,
            byte[] body,
            ClientHttpResponse response,
            Throwable error) {
        try {
            doAfterResponse(request, body, response, error);
        } catch (Exception ex) {
            log.error("记录remote调用事件失败，请求地址：{}", request.getURI(), ex);
        }
    }

    private void doAfterResponse(
            HttpRequest request,
            byte[] body,
            ClientHttpResponse response,
            Throwable error) throws IOException {
        RemoteInterfaceProperties.ObservationProperties observationProperties = remoteInterfaceProperties.getObservation();
        final RemoteCallEvent event = new RemoteCallEvent();
        event.setHttpMethod(request.getMethod().name());
        event.setHttpUri(request.getURI().toString());
        Map<String, String> requestHeaders = request.getHeaders().asSingleValueMap();
        event.setRequestHeaders(requestHeaders);
        String requestBody = formatBody(body);
        event.setRequestBody(requestBody);
        if (response != null) {
            int httpStatusCode = response.getStatusCode().value();
            event.setHttpStatusCode(httpStatusCode);
            Map<String, String> responseHeaders = response.getHeaders().asSingleValueMap();
            event.setResponseHeaders(responseHeaders);
            String responseBody = formatBody(StreamUtils.copyToByteArray(response.getBody()));
            event.setResponseBody(responseBody);
        }

        long costTime = resolveCostTime(request);
        event.setCostTime(costTime);
        event.setErrorMessage(error == null ? null : error.getClass().getName() + ": " + error.getMessage());

        if (observationProperties.isLogEnabled()) {
            if (error == null) {
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
                        request.getURI(),
                        requestHeaders,
                        requestBody,
                        event.getHttpStatusCode(),
                        event.getResponseHeaders(),
                        event.getResponseBody(),
                        costTime);
            } else {
                log.error("""
                                Http Service Client call failed =>
                                request url: {},
                                request headers: {},
                                request body: {},
                                error: {},
                                cost: {} ms
                                """,
                        request.getURI(),
                        requestHeaders,
                        requestBody,
                        event.getErrorMessage(),
                        costTime,
                        error);
            }
        }

        publisher.publishEvent(event);
    }

    private long resolveCostTime(HttpRequest request) {
        String requestTime = request.getHeaders().getFirst(CustomHeaderConstants.X_REQUEST_TIME);
        if (requestTime == null) {
            return -1L;
        }
        try {
            return System.currentTimeMillis() - Long.parseLong(requestTime);
        } catch (NumberFormatException ex) {
            return -1L;
        }
    }

    private String formatBody(byte[] body) {
        String payload = new String(body, StandardCharsets.UTF_8);
        int maxBodyLength = remoteInterfaceProperties.getObservation().getMaxBodyLength();
        if (maxBodyLength < 0 || payload.length() <= maxBodyLength) {
            return payload;
        }
        return payload.substring(0, maxBodyLength) + "...(truncated)";
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
