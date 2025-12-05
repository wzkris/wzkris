package com.wzkris.common.httpservice.interceptor.core;

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
import java.util.stream.Collectors;

@Slf4j
public class HttpServiceClientInterceptor implements ClientHttpRequestInterceptor {

    private final List<InterceptorPostProcessor> interceptorPostProcessors;

    public HttpServiceClientInterceptor(List<InterceptorPostProcessor> interceptorPostProcessors) {
        if (CollectionUtils.isNotEmpty(interceptorPostProcessors)) {
            this.interceptorPostProcessors = interceptorPostProcessors.stream()
                    .sorted(Comparator.comparing(Ordered::getOrder)).toList();

            log.info("加载到http service client拦截器，总数：{}，列表：{}",
                    this.interceptorPostProcessors.size(),
                    this.interceptorPostProcessors.stream()
                            .map(p -> p.getClass().getSimpleName() + "(order:" + p.getOrder() + ")")
                            .collect(Collectors.joining(" → ")));
        } else {
            this.interceptorPostProcessors = new ArrayList<>();
        }
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        // 创建责任链上下文
        InterceptorChainContext context = new InterceptorChainContext(request, body, execution);

        // 执行责任链
        return context.execute();
    }

    /**
     * 责任链上下文类
     */
    private class InterceptorChainContext {

        private final HttpRequest request;

        private final byte[] body;

        private final ClientHttpRequestExecution execution;

        private int currentIndex = 0;

        public InterceptorChainContext(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
            this.request = request;
            this.body = body;
            this.execution = execution;
        }

        public ClientHttpResponse execute() throws IOException {
            if (currentIndex < interceptorPostProcessors.size()) {
                // 执行前置处理并继续链条
                InterceptorPostProcessor processor = interceptorPostProcessors.get(currentIndex++);
                processor.postHandleBeforeRequest(request, body);

                // 继续执行下一个处理器
                ClientHttpResponse response = this.execute();

                // 执行后置处理（这里形成了"回程"）
                processor.postHandleAfterResponse(request, body, response);

                return response;
            } else {
                // 所有前置处理完成，执行实际请求
                return execution.execute(request, body);
            }
        }

    }

}