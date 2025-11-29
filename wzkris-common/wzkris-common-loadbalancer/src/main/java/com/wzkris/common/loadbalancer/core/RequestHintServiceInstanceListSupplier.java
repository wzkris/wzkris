package com.wzkris.common.loadbalancer.core;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.core.DelegatingServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.HintBasedServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 解决MVC不能从当前请求中获取hint的问题，从请求头中获取
 *
 * @author wzkris
 * @see HintBasedServiceInstanceListSupplier
 */
@Slf4j
public class RequestHintServiceInstanceListSupplier extends DelegatingServiceInstanceListSupplier {

    private final LoadBalancerProperties properties;

    public RequestHintServiceInstanceListSupplier(ServiceInstanceListSupplier delegate,
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
        return delegate.get(request).map(instances -> filteredByHint(instances, getHint()));
    }

    private String getHint() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
            return httpServletRequest.getHeader(properties.getHintHeaderName());
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
