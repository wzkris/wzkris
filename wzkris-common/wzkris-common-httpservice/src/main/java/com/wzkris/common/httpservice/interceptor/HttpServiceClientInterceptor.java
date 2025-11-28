package com.wzkris.common.httpservice.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Common interceptor applied to all load-balanced {@link org.springframework.web.client.RestClient} instances.
 */
@Slf4j
public class HttpServiceClientInterceptor implements ClientHttpRequestInterceptor {

    private final List<InterceptorPostProcessor> interceptorPostProcessors;

    public HttpServiceClientInterceptor(List<InterceptorPostProcessor> interceptorPostProcessors) {
        if (CollectionUtils.isNotEmpty(interceptorPostProcessors)) {
            this.interceptorPostProcessors = interceptorPostProcessors.stream()
                    .sorted(Comparator.comparing(Ordered::getOrder)).toList();
        } else {
            this.interceptorPostProcessors = new ArrayList<>();
        }
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        for (InterceptorPostProcessor interceptorPostProcessor : interceptorPostProcessors) {
            interceptorPostProcessor.postHandleBeforeRequest(request, body);
        }

        try (ClientHttpResponse response = execution.execute(request, body)) {

            for (InterceptorPostProcessor interceptorPostProcessor : interceptorPostProcessors) {
                interceptorPostProcessor.postHandleAfterResponse(request, body, response);
            }

            return response;
        }
    }

}

