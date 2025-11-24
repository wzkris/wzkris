package com.wzkris.common.httpservice.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Enables scanning for {@link HttpServiceClient} annotated interfaces.
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Import(HttpServiceClientsRegistrar.class)
public @interface EnableHttpServiceClients {

    /**
     * Base packages to scan.
     */
    String[] basePackages() default {"com.wzkris"};

    /**
     * Base package classes to derive packages from.
     */
    Class<?>[] basePackageClasses() default {};

}

