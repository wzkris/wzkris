package com.wzkris.gateway.utils;

import com.wzkris.common.core.utils.SpringUtil;
import org.springframework.aop.support.AopUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class ScanAnnotationUrlUtil {

    /**
     * 获取注解标记的方法的URL映射
     */
    public static Set<String> scanUrls(Class<? extends Annotation> annotation) {
        Set<String> url = new TreeSet<>();

        String[] beanNames = SpringUtil.getContext().getBeanDefinitionNames();

        for (String beanName : beanNames) {
            Object bean = SpringUtil.getContext().getBean(beanName);
            Class<?> beanClass = AopUtils.getTargetClass(bean);

            // 获取类级别的URL前缀
            String classUrlPrefix = getClassUrlMapping(beanClass);

            Method[] methods = beanClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(annotation) && isValidMethod(method)) {
                    Set<String> methodUrls = getMethodUrls(method, classUrlPrefix);
                    url.addAll(methodUrls);
                }
            }

            // 处理类级别注解
            if (beanClass.isAnnotationPresent(annotation)) {
                for (Method method : methods) {
                    if (isValidMethod(method) && Modifier.isPublic(method.getModifiers())) {
                        Set<String> methodUrls = getMethodUrls(method, classUrlPrefix);
                        url.addAll(methodUrls);
                    }
                }
            }
        }

        return url;
    }

    /**
     * 获取类级别的URL映射前缀
     */
    private static String getClassUrlMapping(Class<?> clazz) {
        // 检查RequestMapping注解
        RequestMapping classMapping = clazz.getAnnotation(RequestMapping.class);
        if (classMapping != null && classMapping.value().length > 0) {
            return normalizeUrl(classMapping.value()[0]);
        }

        // 检查其他映射注解
        GetMapping getMapping = clazz.getAnnotation(GetMapping.class);
        if (getMapping != null && getMapping.value().length > 0) {
            return normalizeUrl(getMapping.value()[0]);
        }

        PostMapping postMapping = clazz.getAnnotation(PostMapping.class);
        if (postMapping != null && postMapping.value().length > 0) {
            return normalizeUrl(postMapping.value()[0]);
        }

        PutMapping putMapping = clazz.getAnnotation(PutMapping.class);
        if (putMapping != null && putMapping.value().length > 0) {
            return normalizeUrl(putMapping.value()[0]);
        }

        DeleteMapping deleteMapping = clazz.getAnnotation(DeleteMapping.class);
        if (deleteMapping != null && deleteMapping.value().length > 0) {
            return normalizeUrl(deleteMapping.value()[0]);
        }

        return "";
    }

    /**
     * 获取方法级别的URL映射
     */
    private static Set<String> getMethodUrls(Method method, String classUrlPrefix) {
        Set<String> urls = new HashSet<>();

        // 检查RequestMapping注解
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            String[] methodPaths = requestMapping.value();
            if (methodPaths.length > 0) {
                for (String path : methodPaths) {
                    urls.add(buildFullUrl(classUrlPrefix, path));
                }
            } else {
                urls.add(classUrlPrefix);
            }
            return urls;
        }

        // 检查GetMapping注解
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        if (getMapping != null && getMapping.value().length > 0) {
            for (String path : getMapping.value()) {
                urls.add(buildFullUrl(classUrlPrefix, path));
            }
            return urls;
        }

        // 检查PostMapping注解
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        if (postMapping != null && postMapping.value().length > 0) {
            for (String path : postMapping.value()) {
                urls.add(buildFullUrl(classUrlPrefix, path));
            }
            return urls;
        }

        // 检查PutMapping注解
        PutMapping putMapping = method.getAnnotation(PutMapping.class);
        if (putMapping != null && putMapping.value().length > 0) {
            for (String path : putMapping.value()) {
                urls.add(buildFullUrl(classUrlPrefix, path));
            }
            return urls;
        }

        // 检查DeleteMapping注解
        DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
        if (deleteMapping != null && deleteMapping.value().length > 0) {
            for (String path : deleteMapping.value()) {
                urls.add(buildFullUrl(classUrlPrefix, path));
            }
            return urls;
        }

        // 如果没有方法级别的映射，使用类级别的
        if (!classUrlPrefix.isEmpty()) {
            urls.add(classUrlPrefix);
        }

        return urls;
    }

    /**
     * 构建完整的URL
     */
    private static String buildFullUrl(String classUrl, String methodUrl) {
        if (classUrl.isEmpty()) {
            return normalizeUrl(methodUrl);
        }
        if (methodUrl.isEmpty()) {
            return normalizeUrl(classUrl);
        }
        return normalizeUrl(classUrl) + normalizeUrl(methodUrl);
    }

    /**
     * 规范化URL（确保以/开头，不以/结尾）
     */
    private static String normalizeUrl(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }

        // 确保以/开头
        if (!url.startsWith("/")) {
            url = "/" + url;
        }

        // 移除末尾的/
        if (url.endsWith("/") && url.length() > 1) {
            url = url.substring(0, url.length() - 1);
        }

        return url;
    }

    /**
     * 构建方法的全限定名
     */
    private static String buildFullMethodName(Class<?> clazz, Method method) {
        return String.format("%s.%s(%s)",
                clazz.getName(),
                method.getName(),
                Arrays.stream(method.getParameterTypes())
                        .map(Class::getSimpleName)
                        .collect(Collectors.joining(","))
        );
    }

    private static boolean isValidMethod(Method method) {
        return !method.getDeclaringClass().equals(Object.class)
                && !method.isSynthetic()
                && !method.isBridge();
    }

    /**
     * 获取PermitAll注解标记的方法详细信息（包含URL和方法信息）
     */
    public static Map<String, String> scanMethodsWithUrls(Class<? extends Annotation> annotation) {
        Map<String, String> permitAllMethods = new LinkedHashMap<>();

        String[] beanNames = SpringUtil.getContext().getBeanDefinitionNames();

        for (String beanName : beanNames) {
            Object bean = SpringUtil.getContext().getBean(beanName);
            Class<?> beanClass = AopUtils.getTargetClass(bean);

            String classUrlPrefix = getClassUrlMapping(beanClass);

            Method[] methods = beanClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(annotation) && isValidMethod(method)) {
                    Set<String> methodUrls = getMethodUrls(method, classUrlPrefix);
                    String methodInfo = buildFullMethodName(beanClass, method);

                    for (String url : methodUrls) {
                        permitAllMethods.put(url, methodInfo);
                    }
                }
            }
        }

        return permitAllMethods;
    }

}