package com.wzkris.common.openfeign.interceptor;

import com.wzkris.common.openfeign.config.IdentityProperties;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : openfeign拦截器
 * @date : 2023/8/4 10:46
 */
public class OpenFeignInterceptor implements RequestInterceptor {

    private final IdentityProperties identityProperties;

    public OpenFeignInterceptor(IdentityProperties identityProperties) {
        this.identityProperties = identityProperties;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(identityProperties.getIdentityKey(), identityProperties.getIdentityValue());
    }
}
