package com.wzkris.common.remote.annotation;

import com.wzkris.common.remote.config.RemoteInterfaceRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Enables scanning for {@link RemoteInterface} annotated interfaces.
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Import(RemoteInterfaceRegistrar.class)
public @interface EnableRemoteInterfaces {

    /**
     * Base packages to scan.
     */
    String[] basePackages() default {"com.wzkris"};

    /**
     * Base package classes to derive packages from.
     */
    Class<?>[] basePackageClasses() default {};

}
