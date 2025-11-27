package com.wzkris.common.loadbalancer.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;

@LoadBalancerClients(defaultConfiguration = HintServiceSupplierConfig.class)
public class CustomAutoConfiguration {

}
