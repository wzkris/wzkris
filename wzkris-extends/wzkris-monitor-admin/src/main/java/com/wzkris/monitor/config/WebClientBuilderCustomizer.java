package com.wzkris.monitor.config;

import com.wzkris.common.apikey.config.SignkeyProperties;
import com.wzkris.common.apikey.utils.RequestSignerUtil;
import com.wzkris.monitor.request.CapturingClientHttpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.function.BiConsumer;

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

    private final SignkeyProperties signProp;

    @Value("${spring.application.name}")
    String applicationName;

    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        webClientBuilder.filter((request, next) -> {
            // 大多数 GET/DELETE 没 body，直接按 null 签名
            if (HttpMethod.GET.equals(request.method()) || HttpMethod.DELETE.equals(request.method())) {
                ClientRequest newClientRequest = ClientRequest.from(request)
                        .headers(headers -> applySignatureHeaders(headers::add, null))
                        .build();
                return next.exchange(newClientRequest);
            }

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
                                        .headers(h -> applySignatureHeaders(h::add, bodyStr))
                                        // 重新注入 body（字符串），保证下游能读到
                                        .body(BodyInserters.fromValue(bodyStr == null ? "" : bodyStr))
                                        .build();
                            }))
                            .flatMap(next::exchange)
            );
        });
    }

    /**
     * 公共方法：往 HttpHeaders 里加签名头
     */
    private void applySignatureHeaders(BiConsumer<String, String> headerSetter,
                                       String body) {
        RequestSignerUtil.setCommonHeaders(
                headerSetter,
                applicationName,
                signProp.getKeys().get(applicationName).getSecret(),
                body,
                System.currentTimeMillis()
        );
    }

}
