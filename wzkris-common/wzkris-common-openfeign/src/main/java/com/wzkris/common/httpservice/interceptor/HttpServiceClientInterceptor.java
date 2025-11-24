package com.wzkris.common.httpservice.interceptor;

import com.wzkris.common.apikey.config.SignkeyProperties;
import com.wzkris.common.apikey.utils.RequestSignerUtil;
import com.wzkris.common.openfeign.enums.BizRpcCodeEnum;
import com.wzkris.common.openfeign.event.FeignRequestEvent;
import com.wzkris.common.openfeign.event.FeignResponseEvent;
import com.wzkris.common.openfeign.exception.RpcException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Common interceptor applied to all load-balanced {@link org.springframework.web.client.RestClient} instances.
 */
@Slf4j
@RequiredArgsConstructor
public class HttpServiceClientInterceptor implements ClientHttpRequestInterceptor {

    private final SignkeyProperties signkeyProperties;

    private final ApplicationEventPublisher publisher;

    private final String applicationName;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        long requestTime = System.currentTimeMillis();
        handleRequest(request, body, requestTime);

        try (ClientHttpResponse response = execution.execute(request, body)) {

            return handleResponse(request, response, requestTime);
        }
    }

    private void handleRequest(HttpRequest request, byte[] body, long requestTime) {
        RequestSignerUtil.setCommonHeaders(
                request.getHeaders()::add,
                applicationName,
                resolveAppSecret(),
                body != null ? new String(body, StandardCharsets.UTF_8) : "",
                requestTime
        );

        publishRequestEvent(request, body, requestTime);
    }

    private ClientHttpResponse handleResponse(HttpRequest request,
                                              ClientHttpResponse response,
                                              long requestTime) throws IOException {
        long costTime = System.currentTimeMillis() - requestTime;
        String responseBody = readBody(response);

        boolean success = response.getStatusCode().is2xxSuccessful();
        publishResponseEvent(response, responseBody, costTime, success);

        if (success) {
            return response;
        }

        log.error("""
                        HttpService call failed =>
                        url: {},
                        status: {},
                        body: {},
                        cost: {} ms
                        """,
                request.getURI(), response.getStatusCode(), responseBody, costTime);

        throw new RpcException(
                response.getStatusCode().value(),
                BizRpcCodeEnum.RPC_REMOTE_ERROR.value(),
                responseBody
        );
    }

    private String readBody(ClientHttpResponse response) {
        try {
            return new String(StreamUtils.copyToByteArray(response.getBody()), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            log.warn("Failed to read response body: {}", ex.getMessage());
            return "";
        }
    }

    private void publishRequestEvent(HttpRequest request, byte[] body, long requestTime) {
        FeignRequestEvent requestEvent = new FeignRequestEvent();
        requestEvent.setUrl(request.getURI().toString());
        requestEvent.setMethod(request.getMethod().name());
        requestEvent.setHeaders(request.getHeaders().entrySet().stream()
                .map(entry -> entry.getKey() + ": " + String.join(", ", entry.getValue()))
                .collect(Collectors.joining("\n")));
        requestEvent.setBody(body != null ? new String(body, StandardCharsets.UTF_8) : "");
        requestEvent.setRequestTime(requestTime);
        publisher.publishEvent(requestEvent);
    }

    private void publishResponseEvent(ClientHttpResponse response,
                                      String body,
                                      long costTime,
                                      boolean success) throws IOException {
        FeignResponseEvent event = new FeignResponseEvent();
        event.setSuccess(success);
        event.setHttpStatusCode(response.getStatusCode().value());
        event.setBody(body);
        event.setCostTime((int) costTime);
        publisher.publishEvent(event);
    }

    private String resolveAppSecret() {
        SignkeyProperties.Sign mySignKey = signkeyProperties.getKeys().get(applicationName);
        Assert.notNull(mySignKey, "signkey not configured for application: " + applicationName);
        return mySignKey.getSecret();
    }

}

