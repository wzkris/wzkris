package com.wzkris.common.httpservice.interceptor;

import com.wzkris.common.apikey.config.SignkeyProperties;
import com.wzkris.common.apikey.utils.RequestSignerUtil;
import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.exception.service.GenericException;
import com.wzkris.common.httpservice.enums.BizRpcCodeEnum;
import com.wzkris.common.httpservice.event.HttpRequestEvent;
import com.wzkris.common.httpservice.event.HttpResponseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 追加签名头与打印日志
 *
 * @author wzkris
 */
@Slf4j
@RequiredArgsConstructor
public class BaseInterceptorPostProcessor implements InterceptorPostProcessor {

    private final SignkeyProperties signkeyProperties;

    private final ApplicationEventPublisher publisher;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void postHandleBeforeRequest(HttpRequest request, byte[] body) {
        long requestTime = System.currentTimeMillis();
        RequestSignerUtil.setCommonHeaders(
                request.getHeaders()::add,
                applicationName,
                resolveAppSecret(),
                body != null ? new String(body, StandardCharsets.UTF_8) : "",
                requestTime
        );

        publishRequestEvent(request, body, requestTime);
    }

    @Override
    public void postHandleAfterResponse(HttpRequest request, byte[] body, ClientHttpResponse response) throws IOException {
        String first = request.getHeaders().getFirst(CustomHeaderConstants.X_REQUEST_TIME);
        long costTime = System.currentTimeMillis() - Long.parseLong(first);
        String responseBody = readBody(response);

        boolean success = response.getStatusCode().is2xxSuccessful();
        publishResponseEvent(response, responseBody, costTime, success);

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
                request.getURI(), request.getHeaders().asSingleValueMap(), new String(body, StandardCharsets.UTF_8),
                response.getStatusCode(), response.getHeaders().asSingleValueMap(), responseBody, costTime);

        if (!success) {
            throw new GenericException(
                    BizRpcCodeEnum.RPC_REMOTE_ERROR.value(),
                    responseBody
            );
        }
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
        HttpRequestEvent requestEvent = new HttpRequestEvent();
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
        HttpResponseEvent event = new HttpResponseEvent();
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

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
