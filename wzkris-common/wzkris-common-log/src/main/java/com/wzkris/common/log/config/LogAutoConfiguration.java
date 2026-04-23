package com.wzkris.common.log.config;

import com.wzkris.common.log.aspect.OperateLogAspect;
import com.wzkris.common.log.listener.OperateEventListener;
import com.wzkris.common.log.remote.IOperateLogRemote;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class LogAutoConfiguration {

    @Bean
    @ConditionalOnClass(IOperateLogRemote.class)
    public OperateEventListener operateEventListener(IOperateLogRemote operateLogRemote) {
        return new OperateEventListener(operateLogRemote);
    }

    @Bean
    public OperateLogAspect operateLogAspect() {
        return new OperateLogAspect();
    }

}
