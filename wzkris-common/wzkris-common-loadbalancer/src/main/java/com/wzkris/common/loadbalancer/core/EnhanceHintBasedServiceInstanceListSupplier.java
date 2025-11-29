package com.wzkris.common.loadbalancer.core;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.utils.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.HintRequestContext;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.core.DelegatingServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.HintBasedServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
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
public class EnhanceHintBasedServiceInstanceListSupplier extends DelegatingServiceInstanceListSupplier {

    private final HintRequestContext defaultContext = new HintRequestContext();

    public EnhanceHintBasedServiceInstanceListSupplier(ServiceInstanceListSupplier delegate,
                                                       ReactiveLoadBalancer.Factory<ServiceInstance> factory) {
        super(delegate);
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return delegate.get();
    }

    @Override
    public Flux<List<ServiceInstance>> get(Request request) {
        return delegate.get(request).map(instances -> filteredByHint(instances, getHint(request.getContext())));
    }

    /**
     * 增强的 hint 获取逻辑，同时支持从 RequestDataContext 和当前请求头中获取
     */
    private String getHint(Object requestContext) {
        String hint = null;

        // 1. 首先尝试从 RequestDataContext 中获取（适用于 Reactive 环境和 MVC 的 outgoing request）
        if (requestContext instanceof RequestDataContext) {
            hint = getHintFromRequestDataContext((RequestDataContext) requestContext);
        }

        // 2. 如果从 RequestDataContext 中没有获取到，尝试从 HintRequestContext 中获取
        if (!StringUtils.hasText(hint) && requestContext instanceof HintRequestContext) {
            String contextHint = ((HintRequestContext) requestContext).getHint();
            if (!StringUtil.equals(contextHint, defaultContext.getHint())) {// 排除默认值
                hint = contextHint;
            }
        }

        // 3. 如果还没有获取到，尝试从当前请求的请求头中获取（适用于 MVC 环境）
        if (!StringUtils.hasText(hint)) {
            hint = getHintFromCurrentRequest();
        }

        return hint;
    }

    /**
     * 从 RequestDataContext 中获取 hint（适用于 Reactive 环境和 outgoing request）
     */
    private String getHintFromRequestDataContext(RequestDataContext context) {
        if (context.getClientRequest() != null) {
            HttpHeaders headers = context.getClientRequest().getHeaders();
            if (headers != null) {
                return headers.getFirst(CustomHeaderConstants.X_ROUTE_HINT);
            }
        }
        return null;
    }

    /**
     * 从当前请求的请求头中获取 hint（适用于 MVC 环境）
     */
    private String getHintFromCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes) && requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
            return httpServletRequest.getHeader(CustomHeaderConstants.X_ROUTE_HINT);
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

        log.warn("无法找到匹配hint的路由实例: {}, 降级返回全部: {}", hint, instances);
        // If instances cannot be found based on hint,
        // we return all instances retrieved for given service id.
        return instances;
    }

}
