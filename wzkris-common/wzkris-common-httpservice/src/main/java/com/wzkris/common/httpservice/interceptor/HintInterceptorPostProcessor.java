package com.wzkris.common.httpservice.interceptor;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.httpservice.interceptor.core.InterceptorPostProcessor;
import com.wzkris.common.loadbalancer.core.HintContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.http.HttpRequest;

/**
 * 透传hint标签
 */
@Slf4j
@ConditionalOnClass(LoadBalancerProperties.class)
public class HintInterceptorPostProcessor implements InterceptorPostProcessor {

    private final LoadBalancerProperties properties;

    public HintInterceptorPostProcessor(LoadBalancerProperties properties) {
        this.properties = properties;
    }

    @Override
    public void postHandleBeforeRequest(HttpRequest request, byte[] body) {
        String hint = HintContextHolder.get();
        if (StringUtil.isNotBlank(hint)) {
            request.getHeaders().add(properties.getHintHeaderName(), hint);
        }
    }

    @Override
    public int getOrder() {
        return 100;
    }

}
