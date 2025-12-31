package com.wzkris.common.sentinel.config;

import com.wzkris.common.notifier.manager.NotifierManager;
import com.wzkris.common.sentinel.handler.SentinelExceptionHandler;
import com.wzkris.common.sentinel.listener.FlowAlarmEventListener;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(SentinelExceptionHandler.class)
@AutoConfiguration
public class SentinelAutoConfiguration {

    @Bean
    @ConditionalOnBean(NotifierManager.class)
    public FlowAlarmEventListener flowAlarmEventListener(NotifierManager notifierManager) {
        return new FlowAlarmEventListener(notifierManager);
    }

}
