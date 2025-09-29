package com.wzkris.monitor.config;

import com.wzkris.common.apikey.config.SignkeyProperties;
import com.wzkris.common.apikey.utils.RequestSignerUtil;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.web.client.HttpHeadersProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * 追加请求头
 */
@Component
@RequiredArgsConstructor
public class AdminHttpHeadersProvider implements HttpHeadersProvider {

    @Value("${spring.application.name}")
    private String applicationName;

    private final SignkeyProperties signkeyProperties;

    @Override
    public HttpHeaders getHeaders(Instance instance) {
        HttpHeaders headers = new HttpHeaders();

        RequestSignerUtil.setCommonHeaders(headers::add,
                applicationName,
                signkeyProperties.getKeys().get(applicationName).getSecret(),
                null,
                System.currentTimeMillis()
        );// 请求签名 -> 防止伪造请求
        return headers;
    }

}
