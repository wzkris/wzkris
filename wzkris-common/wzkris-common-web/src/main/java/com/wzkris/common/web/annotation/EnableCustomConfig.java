package com.wzkris.common.web.annotation;

import com.wzkris.common.web.annotation.aspect.ControllerStatisticAspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableScheduling
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@Import(ControllerStatisticAspect.class)
public @interface EnableCustomConfig {

}
