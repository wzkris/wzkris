package com.wzkris.common.loadbalancer.config;

import com.wzkris.common.loadbalancer.supplier.CustomHintServiceInstanceListSupplier;
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
    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
            ConfigurableApplicationContext configurableApplicationContext) {
        return ServiceInstanceListSupplier.builder()
                .withDiscoveryClient()
                .withCaching()
                .withHints()
                .with((context, delegate) -> {
                    LoadBalancerClientFactory factory = context.getBean(LoadBalancerClientFactory.class);

                    return new CustomHintServiceInstanceListSupplier(delegate, factory);
                })
                .build(configurableApplicationContext);
    }

}
