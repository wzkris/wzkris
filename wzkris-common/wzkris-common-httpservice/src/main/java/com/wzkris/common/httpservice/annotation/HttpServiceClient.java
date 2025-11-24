package com.wzkris.common.httpservice.annotation;

import com.wzkris.common.httpservice.fallback.HttpServiceFallback;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marker annotation for declarative HTTP interfaces powered by {@link org.springframework.web.service.invoker.HttpServiceProxyFactory}.
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface HttpServiceClient {

    /**
     * Direct service URL, if provided will be used directly instead of service discovery.
     */
    String url() default "";

    /**
     * Logical service id registered to the discovery server.
     * Only used when url is not provided.
     */
    String serviceId() default "";

    /**
     * Optional fallback factory for degraded handling.
     */
    Class<? extends HttpServiceFallback<?>> fallbackFactory()
            default HttpServiceFallback.NoOp.class;

}