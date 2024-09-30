package com.wzkris.common.loadbalancer.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;

@LoadBalancerClients(defaultConfiguration = LoadBalancerConfiguration.class)
public class LoadBalancerAutoConfiguration {
}
