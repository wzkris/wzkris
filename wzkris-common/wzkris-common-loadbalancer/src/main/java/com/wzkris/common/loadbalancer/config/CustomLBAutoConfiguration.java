package com.wzkris.common.loadbalancer.config;

import com.wzkris.common.loadbalancer.core.HintLoadBalancerRequestTransformer;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Import;

@Import({HintLoadBalancerRequestTransformer.class})
@LoadBalancerClients(defaultConfiguration = HintServiceSupplierConfig.class)
public class CustomLBAutoConfiguration {

}
