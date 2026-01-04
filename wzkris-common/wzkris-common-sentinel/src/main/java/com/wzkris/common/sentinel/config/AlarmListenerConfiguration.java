package com.wzkris.common.sentinel.config;

import com.wzkris.common.notifier.core.NotifierManager;
import com.wzkris.common.sentinel.listener.FlowAlarmEventListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 允许不引入notifier配置
 */
@Configuration
@ConditionalOnClass(NotifierManager.class)
public class AlarmListenerConfiguration {

    @Bean
    @ConditionalOnBean(NotifierManager.class)
    public FlowAlarmEventListener flowAlarmEventListener(NotifierManager notifierManager) {
        return new FlowAlarmEventListener(notifierManager);
    }

}
