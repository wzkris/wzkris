package com.wzkris.common.httpservice.factory;

import com.wzkris.common.httpservice.fallback.HttpServiceFallbackFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * FactoryBean that creates load-balanced HTTP service proxies.
 */
public class HttpServiceClientFactoryBean<T> implements FactoryBean<T>, InitializingBean,
        BeanFactoryAware, ApplicationContextAware {

    private static final String REST_CLIENT_BUILDER_BEAN = "httpServiceRestClientBuilder";

    private Class<T> type;

    private String serviceId;

    private Class<? extends HttpServiceFallbackFactory<?>> fallbackFactoryClass;

    private BeanFactory beanFactory;

    private ApplicationContext applicationContext;

    private T proxy;

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(type, "HttpService interface type must be provided");
        Assert.hasText(serviceId, "serviceId must not be empty");

        RestClient.Builder builder = getRestClientBuilder();
        RestClient restClient = builder.baseUrl(buildBaseUrl(serviceId)).build();

        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        T target = proxyFactory.createClient(type);
        this.proxy = wrapWithFallbackIfNecessary(target);
    }

    private RestClient.Builder getRestClientBuilder() {
        return applicationContext.getBean(REST_CLIENT_BUILDER_BEAN, RestClient.Builder.class);
    }

    private String buildBaseUrl(String serviceId) {
        if (serviceId.startsWith("http://") || serviceId.startsWith("https://")) {
            return serviceId;
        }
        return "http://" + serviceId;
    }

    private T wrapWithFallbackIfNecessary(T target) {
        if (fallbackFactoryClass == null
                || HttpServiceFallbackFactory.NoOp.class.equals(fallbackFactoryClass)) {
            return target;
        }
        HttpServiceFallbackFactory<T> fallbackFactory = getFallbackFactory();
        InvocationHandler handler = new FallbackInvocationHandler<>(target, fallbackFactory);
        return (T) Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class[]{type},
                handler
        );
    }

    private HttpServiceFallbackFactory<T> getFallbackFactory() {
        if (!(beanFactory instanceof ListableBeanFactory listableBeanFactory)) {
            throw new IllegalStateException("BeanFactory must be ListableBeanFactory");
        }
        return (HttpServiceFallbackFactory<T>) listableBeanFactory.getBean(fallbackFactoryClass);
    }

    @Override
    public T getObject() {
        return proxy;
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

    public void setFallbackFactoryClass(Class<? extends HttpServiceFallbackFactory<?>> fallbackFactoryClass) {
        this.fallbackFactoryClass = fallbackFactoryClass;
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

        private final HttpServiceFallbackFactory<T> fallbackFactory;

        private FallbackInvocationHandler(T target, HttpServiceFallbackFactory<T> fallbackFactory) {
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

