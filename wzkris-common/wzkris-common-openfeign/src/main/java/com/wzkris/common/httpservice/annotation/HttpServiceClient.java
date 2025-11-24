package com.wzkris.common.httpservice.annotation;

import com.wzkris.common.httpservice.fallback.HttpServiceFallbackFactory;
import org.springframework.core.annotation.AliasFor;

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
     * Alias for {@link #serviceId()}.
     */
    @AliasFor("serviceId")
    String value() default "";

    /**
     * Logical service id registered to the discovery server.
     */
    @AliasFor("value")
    String serviceId() default "";

    /**
     * Unique bean name/context id.
     */
    String contextId() default "";

    /**
     * Optional fallback factory for degraded handling.
     */
    Class<? extends HttpServiceFallbackFactory<?>> fallbackFactory()
            default HttpServiceFallbackFactory.NoOp.class;

}

