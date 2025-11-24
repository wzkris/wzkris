package com.wzkris.gateway.config;

import com.wzkris.common.httpservice.constants.ServiceIdConstant;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.DeferringLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebclientConfig {

    @Bean
    @LoadBalanced
    public WebClient tokenWebclient(DeferringLoadBalancerExchangeFilterFunction<?> function) {
        return WebClient.builder()
                .baseUrl("http://" + ServiceIdConstant.AUTH)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                })
                .filter(function)
                .build();
    }

}
