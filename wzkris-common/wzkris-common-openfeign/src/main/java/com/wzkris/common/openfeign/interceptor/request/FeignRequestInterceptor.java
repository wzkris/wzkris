package com.wzkris.common.openfeign.interceptor.request;

import com.wzkris.common.apikey.config.SignkeyProperties;
import com.wzkris.common.apikey.utils.RequestSignerUtil;
import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.utils.TraceIdUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : openfeign拦截器
 * @date : 2023/8/4 10:46
 */
@Slf4j
@RequiredArgsConstructor
public class FeignRequestInterceptor implements RequestInterceptor {

    @Value("${spring.application.name}")
    private String applicationName;

    private final SignkeyProperties signkeyProperties;

    @Override
    public void apply(RequestTemplate template) {
        FeignClient feignClient = AnnotationUtils.getAnnotation(template.feignTarget().type(), FeignClient.class);
        // 添加请求头
        appendRequestHeader(template, feignClient);

        // 打印请求信息
        logRequestInfo(template, feignClient);
    }

    private void appendRequestHeader(RequestTemplate template, FeignClient feignClient) {
        long l = System.currentTimeMillis();
        template.header(HeaderConstants.X_TRACING_ID, TraceIdUtil.get());// 添加追踪ID
        template.header(HeaderConstants.X_REQUEST_TIME, String.valueOf(l));// 添加请求时间
        template.header(HeaderConstants.X_REQUEST_FROM, applicationName);// 请求来源服务
        template.header(HeaderConstants.X_REQUEST_SIGN, RequestSignerUtil.generateSignature(
                signkeyProperties.getKeys().get(applicationName).getSecret(),
                feignClient.path() + template.url(),
                getRequestBody(template),
                l
        ));// 请求签名
    }

    /**
     * 打印Feign请求信息
     *
     * @param template 请求模板
     */
    private void logRequestInfo(RequestTemplate template, FeignClient feignClient) {
        try {
            // 获取请求路径
            String path = feignClient.path() + template.url();

            // 获取请求方法
            String method = template.method();

            // 获取请求头（每个键值对单独一行）
            String headers = "";
            if (!template.headers().isEmpty()) {
                headers = template.headers().entrySet().stream()
                        .map(entry -> entry.getKey() + ": " + String.join(", ", entry.getValue()))
                        .collect(Collectors.joining("\n"));
            }

            // 获取请求参数
            StringBuilder paramsBuilder = new StringBuilder();
            Map<String, Collection<String>> queries = template.queries();
            if (queries != null && !queries.isEmpty()) {
                queries.forEach((key, values) -> {
                    if (values != null) {
                        values.forEach(value -> {
                            paramsBuilder.append(key).append("=").append(value).append("&");
                        });
                    }
                });
                // 删除最后一个&
                if (!paramsBuilder.isEmpty()) {
                    paramsBuilder.deleteCharAt(paramsBuilder.length() - 1);
                }
            }

            String params = paramsBuilder.toString();

            // 获取请求体（对于POST/PUT请求）
            String body = getRequestBody(template);

            // 记录请求日志（包含请求头）
            log.info("""
                            Feign call => Request URL: {}
                            Request Method: {}
                            Request Headers: {}
                            Request Parameters: {}
                            Request Body: {}
                             """,
                    path, method, headers, params, body);
        } catch (Exception e) {
            log.warn("Feign Request => 打印请求发生异常: {}", e.getMessage(), e);
        }
    }

    private String getRequestBody(RequestTemplate template) {
        String body = "";
        if (template.body() != null) {
            body = new String(template.body(), template.requestCharset());
        }
        return body;
    }

}