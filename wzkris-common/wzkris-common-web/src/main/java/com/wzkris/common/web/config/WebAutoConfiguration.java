package com.wzkris.common.web.config;

import com.wzkris.common.web.aspect.ControllerStatisticAspect;
import com.wzkris.common.web.filter.TraceIdFilter;
import com.wzkris.common.web.handler.RestExceptionHandler;
import com.wzkris.common.web.utils.UserAgentUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@Import({ControllerStatisticAspect.class, RestExceptionHandler.class,
        JacksonConfig.class, UserAgentUtil.class})
@AutoConfiguration
public class WebAutoConfiguration {

    @Bean
    public FilterRegistrationBean<TraceIdFilter> commonRequestAndResponseFilter() {
        FilterRegistrationBean<TraceIdFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TraceIdFilter());
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

}
