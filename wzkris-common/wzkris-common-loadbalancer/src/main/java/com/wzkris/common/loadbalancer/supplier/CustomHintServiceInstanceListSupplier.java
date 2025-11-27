package com.wzkris.common.loadbalancer.supplier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.HintRequestContext;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.core.DelegatingServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * 扩展hint，从上下文中获取
 */
@Slf4j
public class CustomHintServiceInstanceListSupplier extends DelegatingServiceInstanceListSupplier {

    private final LoadBalancerProperties properties;

    public CustomHintServiceInstanceListSupplier(ServiceInstanceListSupplier delegate,
                                                 ReactiveLoadBalancer.Factory<ServiceInstance> factory) {
        super(delegate);
        this.properties = factory.getProperties(getServiceId());
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return delegate.get();
    }

    @Override
    public Flux<List<ServiceInstance>> get(Request request) {
        return delegate.get(request).map(instances -> filteredByHint(instances, getHint(request.getContext())));
    }

    private String getHint(Object requestContext) {
        if (requestContext == null) {
            return null;
        }
        String hint = null;
        if (requestContext instanceof RequestDataContext) {
            hint = getHintFromHeader((RequestDataContext) requestContext);
        }
        if (!StringUtils.hasText(hint) && requestContext instanceof HintRequestContext) {
            hint = ((HintRequestContext) requestContext).getHint();
        }
        return hint;
    }

    private String getHintFromHeader(RequestDataContext context) {
        if (context.getClientRequest() != null) {
            HttpHeaders headers = context.getClientRequest().getHeaders();
            if (headers != null) {
                return headers.getFirst(properties.getHintHeaderName());
            }
        }
        return null;
    }

    private List<ServiceInstance> filteredByHint(List<ServiceInstance> instances, String hint) {
        if (!StringUtils.hasText(hint)) {
            return instances;
        }
        List<ServiceInstance> filteredInstances = new ArrayList<>();
        for (ServiceInstance serviceInstance : instances) {
            if (serviceInstance.getMetadata().getOrDefault("hint", "").equals(hint)) {
                filteredInstances.add(serviceInstance);
            }
        }
        if (filteredInstances.size() > 0) {
            return filteredInstances;
        }

        // If instances cannot be found based on hint,
        // we return all instances retrieved for given service id.
        return instances;
    }

}
