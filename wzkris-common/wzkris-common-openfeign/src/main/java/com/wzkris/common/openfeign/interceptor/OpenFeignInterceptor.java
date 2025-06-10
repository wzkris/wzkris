package com.wzkris.common.openfeign.interceptor;

import com.wzkris.common.core.constant.CommonConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : openfeign拦截器
 * @date : 2023/8/4 10:46
 */
public class OpenFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(CommonConstants.X_TRACING_ID, MDC.get(CommonConstants.X_TRACING_ID));
        requestTemplate.header(CommonConstants.X_INNER_REQUEST, "true");
    }
}
