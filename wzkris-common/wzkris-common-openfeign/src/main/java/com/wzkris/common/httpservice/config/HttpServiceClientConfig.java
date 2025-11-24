package com.wzkris.common.httpservice.config;

import com.wzkris.common.apikey.config.SignkeyProperties;
import com.wzkris.common.httpservice.annotation.EnableHttpServiceClients;
import com.wzkris.common.httpservice.interceptor.HttpServiceClientInterceptor;
import com.wzkris.common.openfeign.config.FeignClientProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * Shared configuration for declarative HTTP service clients.
 */
@Configuration
@EnableHttpServiceClients
public class HttpServiceClientConfig {

    @Bean
    public static HttpServiceClientInterceptor httpServiceClientInterceptor(SignkeyProperties signkeyProperties,
                                                                            ApplicationEventPublisher publisher,
                                                                            @Value("${spring.application.name}")
                                                                            String applicationName) {
        return new HttpServiceClientInterceptor(signkeyProperties, publisher, applicationName);
    }

    @Bean(name = "httpServiceRestClientBuilder")
    @Scope(SCOPE_PROTOTYPE)
    @LoadBalanced
    public static RestClient.Builder httpServiceRestClientBuilder(HttpServiceClientInterceptor interceptor,
                                                                  FeignClientProperties feignClientProperties,
                                                                  LoadBalancerInterceptor loadBalancerInterceptor) {
        ClientHttpRequestFactory requestFactory =
                new BufferingClientHttpRequestFactory(buildSimpleFactory(feignClientProperties));
        return RestClient.builder()
                .requestFactory(requestFactory)
                .requestInterceptor(loadBalancerInterceptor)
                .requestInterceptor(interceptor);
    }

    private static SimpleClientHttpRequestFactory buildSimpleFactory(FeignClientProperties feignClientProperties) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(feignClientProperties.getReadTimeout());
        factory.setConnectTimeout(feignClientProperties.getConnectTimeout());
        return factory;
    }

}

