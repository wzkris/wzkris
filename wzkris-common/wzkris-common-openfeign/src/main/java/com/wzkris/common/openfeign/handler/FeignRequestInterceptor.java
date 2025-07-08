package com.wzkris.common.openfeign.handler;

import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.openfeign.constants.FeignHeaderConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : openfeign拦截器
 * @date : 2023/8/4 10:46
 */
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header(HeaderConstants.X_TRACING_ID, MDC.get(HeaderConstants.X_TRACING_ID));
        template.header(FeignHeaderConstant.X_INNER_REQUEST, "true");
    }

}
