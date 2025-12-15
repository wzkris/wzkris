package com.wzkris.monitor.config;

import com.wzkris.monitor.request.CapturingClientHttpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * SBA官方扩展点
 * <p></p>
 * 对请求进行修改
 *
 * @author wzkris
 */
@Configuration
@RequiredArgsConstructor
public class WebClientBuilderCustomizer implements WebClientCustomizer {

    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        webClientBuilder.filter((request, next) -> {
            CapturingClientHttpRequest capture = new CapturingClientHttpRequest(request.method(), request.url());

            // 将 body 写入 capture，然后在完成后取出 body 字符串，签名并重建请求
            return Mono.defer(() ->
                    request.body().insert(capture, new BodyInserter.Context() {
                                @Override
                                public List<HttpMessageWriter<?>> messageWriters() {
                                    // 空列表即可，我们只捕获数据，不真正序列化
                                    return Collections.emptyList();
                                }

                                @Override
                                public Optional<ServerHttpRequest> serverRequest() {
                                    return Optional.empty();
                                }

                                @Override
                                public Map<String, Object> hints() {
                                    return Collections.emptyMap();
                                }
                            })
                            .then(Mono.fromCallable(() -> {
                                String bodyStr = capture.getBodyAsString();
                                return ClientRequest.from(request)
//                                        .headers(h -> )
                                        // 重新注入 body（字符串），保证下游能读到
                                        .body(BodyInserters.fromValue(bodyStr == null ? "" : bodyStr))
                                        .build();
                            }))
                            .flatMap(next::exchange)
            );
        });
    }

}
