package com.wzkris.common.loadbalancer.config;

import com.wzkris.common.loadbalancer.core.EnhanceHintBasedServiceInstanceListSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author : wzkris
 * @description : 基于标签的负载均衡配置
 * @link https://stackoverflow.com/questions/66534708/configuring-spring-cloud-loadbalancer-without-autoconfiguration/66643909#66643909
 */
public class HintServiceSupplierConfig {

    @Bean
    @ConditionalOnBean(DiscoveryClient.class)
    @ConditionalOnMissingBean
    public ServiceInstanceListSupplier blockingDiscoveryClientServiceInstanceListSupplier(
            ConfigurableApplicationContext configurableApplicationContext) {
        return ServiceInstanceListSupplier.builder()
                .withBlockingDiscoveryClient()
                .withCaching()
                .with((context, delegate) -> {
                    LoadBalancerClientFactory factory = context.getBean(LoadBalancerClientFactory.class);

                    return new EnhanceHintBasedServiceInstanceListSupplier(delegate, factory);
                })
                .build(configurableApplicationContext);
    }

}