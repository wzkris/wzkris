package com.wzkris.common.openfeign.core;

import com.wzkris.common.core.utils.StringUtil;
import feign.Feign;
import feign.Target;
import org.springframework.cloud.openfeign.FeignClientFactory;
import org.springframework.cloud.openfeign.FeignClientFactoryBean;
import org.springframework.cloud.openfeign.Targeter;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * openfeign支持本地调用
 *
 * @author wzkris
 * @date 2025/07/08
 */
public class TargeterDecorator implements Targeter {

    private final Targeter defaultTargeter;

    private final ApplicationContext applicationContext;

    public TargeterDecorator(Targeter targeter, ApplicationContext context) {
        this.defaultTargeter = targeter;
        this.applicationContext = context;
    }

    @Override
    public <T> T target(FeignClientFactoryBean factory, Feign.Builder feign, FeignClientFactory context,
                        Target.HardCodedTarget<T> target) {
        String application = applicationContext.getEnvironment().getProperty("spring.application.name");
        if (StringUtil.equals(target.name(), application)) {
            return (T) Proxy.newProxyInstance(
                    target.type().getClassLoader(),
                    new Class<?>[]{target.type()},
                    new LocalFeignInvocationHandler(target.type(), applicationContext)
            );
        }
        return defaultTargeter.target(factory, feign, context, target);
    }

    static class LocalFeignInvocationHandler implements InvocationHandler {

        private final Class<?> feignInterface;

        private final ApplicationContext applicationContext;

        private volatile Object localImpl;

        private volatile boolean initialized = false;

        public LocalFeignInvocationHandler(
                Class<?> feignInterface,
                ApplicationContext applicationContext) {
            this.feignInterface = feignInterface;
            this.applicationContext = applicationContext;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            findLocalBean();
            if (localImpl == null) {
                throw new UnsupportedOperationException("Local implementation not available");
            }
            return method.invoke(localImpl, args);
        }

        private void findLocalBean() {
            if (initialized) {
                return;
            }
            if (localImpl == null) {
                synchronized (this) {
                    if (localImpl == null) {
                        Map<String, ?> beans = applicationContext.getBeansOfType(feignInterface);
                        localImpl = beans.values().stream()
                                .filter(bean -> !Proxy.isProxyClass(bean.getClass()))
                                .findFirst()
                                .orElse(null);
                        initialized = true;
                    }
                }
            }
        }

    }

}
