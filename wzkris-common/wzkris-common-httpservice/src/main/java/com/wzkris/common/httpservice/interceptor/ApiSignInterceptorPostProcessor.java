package com.wzkris.common.httpservice.interceptor;

import com.wzkris.common.apikey.config.SignkeyProperties;
import com.wzkris.common.apikey.utils.RequestSignerUtil;
import com.wzkris.common.core.utils.TraceIdUtil;
import com.wzkris.common.httpservice.interceptor.core.InterceptorPostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.Ordered;
import org.springframework.http.HttpRequest;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;

/**
 * 追加签名头与打印日志
 *
 * @author wzkris
 */
@Slf4j
@ConditionalOnBean(SignkeyProperties.class)
public class ApiSignInterceptorPostProcessor implements InterceptorPostProcessor {

    private final SignkeyProperties signkeyProperties;

    @Value("${spring.application.name}")
    private String applicationName;

    public ApiSignInterceptorPostProcessor(SignkeyProperties signkeyProperties) {
        this.signkeyProperties = signkeyProperties;
    }

    @Override
    public void postHandleBeforeRequest(HttpRequest request, byte[] body) {
        long requestTime = System.currentTimeMillis();
        RequestSignerUtil.setCommonHeaders(
                request.getHeaders()::add,
                TraceIdUtil.getOrGenerate(),
                applicationName,
                resolveAppSecret(),
                body != null ? new String(body, StandardCharsets.UTF_8) : "",
                requestTime
        );
    }

    private String resolveAppSecret() {
        SignkeyProperties.Sign mySignKey = signkeyProperties.getKeys().get(applicationName);
        Assert.notNull(mySignKey, "signkey not configured for application: " + applicationName);
        return mySignKey.getSecret();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
