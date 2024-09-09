package com.wzkris.common.openfeign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : HTTP请求头拦截器
 * @date : 2023/8/4 10:46
 */
public class HttpHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return;
        }

        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();

        if (headerNames == null) {
            return;
        }

    }
}
