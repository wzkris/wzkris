package com.wzkris.common.openfeign.interceptor.request;

import com.wzkris.common.openfeign.constants.FeignHeaderConstant;
import com.wzkris.common.openfeign.interceptor.AuthenticationTokenUtil;
import com.wzkris.common.openfeign.interceptor.InterceptorCacheUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : openfeign拦截器透传spring-security认证信息
 * @date : 2025/7/8 14:46
 */
public class FeignRequestContextInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        if (InterceptorCacheUtil.checkSkipInterceptor(template)) {
            return;
        }
        SecurityContext context = SecurityContextHolder.getContext();
        if (isNotEmptyContext(context)) {
            String serialize = AuthenticationTokenUtil.serialize(context);
            template.header(FeignHeaderConstant.X_SECURITY_CONTEXT, serialize);
        }
    }

    public static boolean isNotEmptyContext(SecurityContext context) {
        return context != null
                && !context.getClass().isAssignableFrom(SecurityContextHolder.createEmptyContext().getClass());
    }

}
