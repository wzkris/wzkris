package com.wzkris.monitor.config;

import cn.hutool.core.text.StrPool;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.exception.service.GenericException;
import com.wzkris.common.core.utils.StringUtil;
import java.time.Instant;
import lombok.Data;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

public class TokenProvider {

    private final OAuth2ClientProperties properties;

    private String accessToken;

    private Instant expiryTime;

    public TokenProvider(OAuth2ClientProperties properties) {
        this.properties = properties;
    }

    public synchronized void retrieveToken() {
        if (accessToken == null || Instant.now().isAfter(expiryTime)) {
            WebClient webClient = WebClient.create();

            Result<TokenResponse> result = webClient
                    .post()
                    .uri(properties.getUrl())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                            .with("client_id", properties.getClientId())
                            .with("client_secret", properties.getClientSecret())
                            .with("scope", StringUtil.join(StrPool.COMMA, properties.getScopes())))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Result<TokenResponse>>() {})
                    .block();

            if (!result.isSuccess()) {
                throw new GenericException(result.getMessage());
            }

            this.accessToken = result.getData().getAccess_token();
            this.expiryTime = Instant.now().plusSeconds(result.getData().getExpires_in());
        }
    }

    public String getAccessToken() {
        if (accessToken == null || Instant.now().isAfter(expiryTime)) {
            retrieveToken();
        }
        return accessToken;
    }

    @Data
    private static class TokenResponse {

        private String access_token;

        private String token_type;

        private long expires_in;
    }
}
