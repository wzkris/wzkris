package com.wzkris.common.loadbalancer.core;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.utils.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequestTransformer;
import org.springframework.http.HttpRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * 支持lb请求能透传hint头
 * 执行时机为：已选择实例后
 *
 * @author wzkris
 */
public class HintLoadBalancerRequestTransformer implements LoadBalancerRequestTransformer {

    @Override
    public HttpRequest transformRequest(HttpRequest request, ServiceInstance instance) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
            String hint = httpServletRequest.getHeader(CustomHeaderConstants.X_ROUTE_HINT);
            if (StringUtil.isNotBlank(hint)) {
                request.getHeaders().add(CustomHeaderConstants.X_ROUTE_HINT, hint);
            }
        }

        return request;
    }

}
