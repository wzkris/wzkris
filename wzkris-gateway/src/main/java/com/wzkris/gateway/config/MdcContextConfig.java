package com.wzkris.gateway.config;

import com.wzkris.common.core.utils.TraceIdUtil;
import io.micrometer.context.ContextRegistry;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Hooks;

/**
 * 配置webflux的MDC
 */
@Configuration
public class MdcContextConfig implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Hooks.enableAutomaticContextPropagation();
        ContextRegistry.getInstance().registerThreadLocalAccessor(
                "traceId-threadLocal",
                TraceIdUtil::get,
                TraceIdUtil::set,
                TraceIdUtil::clear
        );
    }

}
