package com.wzkris.common.loadbalancer.config;

import com.wzkris.common.loadbalancer.filter.HintContextFilter;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Import;

@Import(HintContextFilter.class)
@LoadBalancerClients(defaultConfiguration = HintServiceSupplierConfig.class)
public class CustomAutoConfiguration {

}
