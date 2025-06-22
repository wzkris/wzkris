package com.wzkris.common.loadbalancer.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.wzkris.common.loadbalancer.core.NacosGrayscaleLoadBalancer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @author : wzkris
 * @date : 2024/09/10 10:12
 * @description : 灰度负载均衡配置
 */
public class LoadBalancerConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReactorLoadBalancer<ServiceInstance> nacosGrayscaleLoadBalancer(
            Environment environment,
            LoadBalancerClientFactory loadBalancerClientFactory,
            NacosDiscoveryProperties nacosDiscoveryProperties) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new NacosGrayscaleLoadBalancer(
                loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class),
                name,
                nacosDiscoveryProperties);
    }
}
