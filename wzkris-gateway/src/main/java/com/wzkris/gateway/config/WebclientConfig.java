package com.wzkris.gateway.config;

import com.wzkris.common.openfeign.constants.FeignHeaderConstant;
import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.DeferringLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebclientConfig {

    @Bean
    @LoadBalanced
    public WebClient tokenWebclient(DeferringLoadBalancerExchangeFilterFunction<?> function) {
        return WebClient.builder()
                .baseUrl("http://" + ServiceIdConstant.AUTH)
                .defaultHeader(FeignHeaderConstant.X_FEIGN_REQUEST, "true")
                .filter(function)
                .build();
    }

}
