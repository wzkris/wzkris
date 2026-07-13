package com.wzkris.gateway.utils;

import com.wzkris.common.core.utils.SpringUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class RequestUrlUtil {

    /**
     * 获取注解标记的方法的URL映射
     */
    public static Set<String> scanAnnotation(Class<? extends Annotation> annotation) {
        Set<String> urls = new TreeSet<>();

        RequestMappingHandlerMapping handlerMapping =
                SpringUtil.getContext().getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();

            // 检查类级别注解
            boolean hasClassAnnotation = handlerMethod.getBeanType().isAnnotationPresent(annotation);
            // 检查方法级别注解
            boolean hasMethodAnnotation = handlerMethod.getMethodAnnotation(annotation) != null;

            if (hasClassAnnotation || hasMethodAnnotation) {
                urls.addAll(entry.getKey().getDirectPaths());
            }
        }

        return urls;
    }

}
