package com.wzkris.common.loadbalancer.config;

import com.wzkris.common.loadbalancer.core.EnhanceHintBasedServiceInstanceListSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * @author : wzkris
 * @description : 基于标签的负载均衡配置
 * @link https://stackoverflow.com/questions/66534708/configuring-spring-cloud-loadbalancer-without-autoconfiguration/66643909#66643909
 */
public class HintServiceSupplierConfig {

    /**
     * webflux用，要保证设置exchange.getRequest().mutate().header(CustomHeaderConstants.X_LB_HINT, version)才会生效
     */
    @Bean
    @Primary // 同时存在以此为准
    @ConditionalOnBean(ReactiveDiscoveryClient.class)
    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
            ConfigurableApplicationContext configurableApplicationContext) {
        return ServiceInstanceListSupplier.builder()
                .withDiscoveryClient()
                .withCaching()
                .with((context, delegate) -> {
                    LoadBalancerClientFactory factory = context.getBean(LoadBalancerClientFactory.class);

                    return new EnhanceHintBasedServiceInstanceListSupplier(delegate, factory);
                })
                .build(configurableApplicationContext);
    }

    /*
     * webmvc用
     */
    @Bean
    @ConditionalOnBean(DiscoveryClient.class)
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
