package com.wzkris.common.web.config;

import com.wzkris.common.web.aspect.ControllerStatisticAspect;
import com.wzkris.common.web.handler.RestExceptionHandler;
import com.wzkris.common.web.utils.UserAgentUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@Import({ControllerStatisticAspect.class, RestExceptionHandler.class,
        JacksonConfig.class, UserAgentUtil.class})
@AutoConfiguration
public class WebAutoConfiguration {

}
