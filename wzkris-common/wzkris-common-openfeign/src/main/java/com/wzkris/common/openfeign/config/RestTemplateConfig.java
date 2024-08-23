package com.wzkris.common.openfeign.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : restTemplate配置类
 * @date : 2022/12/1 10:02
 */
// 开启openfeign
@EnableFeignClients(basePackages = "com.wzkris.*.api")
public class RestTemplateConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        RestTemplateBuilder builder = new RestTemplateBuilder();

        // 增强转换器
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        jackson2HttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
        jackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.TEXT_HTML,
                MediaType.TEXT_PLAIN,
                MediaType.APPLICATION_OCTET_STREAM,
                MediaType.APPLICATION_JSON,
                MediaType.MULTIPART_FORM_DATA,
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.MULTIPART_FORM_DATA));
        builder.setConnectTimeout(Duration.ofSeconds(5));
        builder.setReadTimeout(Duration.ofSeconds(5));
        builder.additionalMessageConverters(
                new StringHttpMessageConverter(StandardCharsets.UTF_8),
                jackson2HttpMessageConverter);
        return builder;
    }

}
