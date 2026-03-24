package com.wzkris.common.log.config;

import com.wzkris.common.log.aspect.OperateLogAspect;
import com.wzkris.common.log.httpclient.OperateLogClient;
import com.wzkris.common.log.listener.OperateEventListener;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class LogAutoConfiguration {

    @Bean
    @ConditionalOnClass(OperateLogClient.class)
    @ConditionalOnBean(OperateLogClient.class)
    public OperateEventListener operateEventListener(OperateLogClient operateLogClient) {
        return new OperateEventListener(operateLogClient);
    }

    @Bean
    public OperateLogAspect operateLogAspect() {
        return new OperateLogAspect();
    }

}
