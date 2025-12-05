package com.wzkris.common.httpservice.config;

import com.wzkris.common.httpservice.fallback.HttpServiceFallback;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 创建HTTP 服务代理的 FactoryBean。
 */
public class HttpServiceClientFactoryBean<T> implements FactoryBean<T>, InitializingBean,
        BeanFactoryAware, ApplicationContextAware {

    private Class<T> type;

    private String url;

    private String serviceId;

    private Class<? extends HttpServiceFallback<?>> fallbackFactory;

    private BeanFactory beanFactory;

    private ApplicationContext applicationContext;

    private volatile T proxy;

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(type, "必须提供 HttpService 接口类型");
        // 验证：必须提供 url 或 serviceId 中的一个
        boolean hasUrl = StringUtils.hasText(url);
        boolean hasServiceId = StringUtils.hasText(serviceId);
        Assert.isTrue(hasUrl || hasServiceId,
                "HttpServiceClient 必须指定 'url' 或 'serviceId' 其中之一");
        if (hasUrl) {
            Assert.isTrue(url.startsWith("http"), "url必须为http开头");
        }
    }

    private RestClient.Builder getRestClientBuilder() {
        return applicationContext.getBean(RestClient.Builder.class);
    }

    private String buildBaseUrl() {
        // 优先使用 url
        if (StringUtils.hasText(url)) {
            return url;
        }
        // 否则使用 serviceId（服务发现）
        return "http://" + serviceId;
    }

    private T wrapWithFallbackIfNecessary(T target) {
        if (fallbackFactory == null
                || HttpServiceFallback.NoOp.class.equals(fallbackFactory)) {
            return target;
        }
        HttpServiceFallback<T> fallbackFactory = getFallbackFactory();
        InvocationHandler handler = new FallbackInvocationHandler<>(target, fallbackFactory);
        return (T) Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class[]{type},
                handler
        );
    }

    private HttpServiceFallback<T> getFallbackFactory() {
        if (!(beanFactory instanceof ListableBeanFactory listableBeanFactory)) {
            throw new IllegalStateException("BeanFactory 必须是 ListableBeanFactory");
        }
        return (HttpServiceFallback<T>) listableBeanFactory.getBean(fallbackFactory);
    }

    public void setFallbackFactory(Class<? extends HttpServiceFallback<?>> fallbackFactory) {
        this.fallbackFactory = fallbackFactory;
    }

    @Override
    public T getObject() {
        if (proxy == null) {
            synchronized (this) {
                if (proxy == null) {
                    proxy = createProxy();
                }
            }
        }
        return proxy;
    }

    private T createProxy() {
        RestClient.Builder builder = getRestClientBuilder();
        RestClient restClient = builder.baseUrl(buildBaseUrl()).build();

        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        T target = proxyFactory.createClient(type);
        return wrapWithFallbackIfNecessary(target);
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private static class FallbackInvocationHandler<T> implements InvocationHandler {

        private final T target;

        private final HttpServiceFallback<T> fallbackFactory;

        private FallbackInvocationHandler(T target, HttpServiceFallback<T> fallbackFactory) {
            this.target = target;
            this.fallbackFactory = fallbackFactory;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                return method.invoke(target, args);
            } catch (Throwable ex) {
                Throwable cause = ex instanceof InvocationTargetException && ex.getCause() != null
                        ? ex.getCause()
                        : ex;
                T fallback = fallbackFactory.create(cause);
                return method.invoke(fallback, args);
            }
        }

    }

}

