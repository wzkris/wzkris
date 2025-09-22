package com.wzkris.monitor.config;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.exception.service.GenericException;
import com.wzkris.common.core.utils.StringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

@Slf4j
public class TokenProvider {

    private final OAuth2ClientProperties properties;

    private final WebClient webClient;

    private String accessToken;

    private Instant expiryTime;

    public TokenProvider(OAuth2ClientProperties properties) {
        this.properties = properties;
        webClient = WebClient.create();
    }

    private boolean isTokenValid() {
        return this.accessToken != null && Instant.now().isBefore(this.expiryTime.minusSeconds(30)); // 提前30秒视为过期
    }

    public Mono<String> getAccessToken() {
        if (isTokenValid()) {
            log.debug("使用缓存的access token");
            return Mono.just(this.accessToken);
        }

        log.debug("获取新的access token");
        return webClient
                .post()
                .uri(properties.getUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                        .with("client_id", properties.getClientId())
                        .with("client_secret", properties.getClientSecret())
                        .with("scope", String.join(StringUtil.COMMA, properties.getScopes())))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Result<OAuth2Response>>() {
                })
                .timeout(Duration.ofSeconds(8)) // 增加超时时间
                .handle((result, sink) -> {
                    if (!result.isSuccess()) {
                        sink.error(new GenericException(result.getMessage()));
                        return;
                    }
                    this.accessToken = result.getData().getAccess_token();
                    this.expiryTime = Instant.now().plusSeconds(result.getData().getExpires_in());
                    sink.next(this.accessToken);
                })
                .cast(String.class)
                .onErrorResume(e -> {
                    log.error("获取oauth2_token失败，异常信息: {}", e.getMessage(), e);
                    // 如果有旧token，尝试使用旧token而不是直接失败
                    if (this.accessToken != null) {
                        log.warn("使用过期的access token作为降级方案");
                        return Mono.just(accessToken);
                    }
                    return Mono.error(new GenericException("获取访问令牌失败: " + e.getMessage()));
                });
    }

    @Data
    private static class OAuth2Response {

        private String access_token;

        private String token_type;

        private long expires_in;

    }

}
