package com.wzkris.common.notifier.config;

import com.wzkris.common.notifier.config.dingtalk.DingtalkConfiguration;
import com.wzkris.common.notifier.config.email.EmailConfiguration;
import com.wzkris.common.notifier.manager.NotifierManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 通知器自动配置
 *
 * @author wzkris
 * @date 2025/11/06
 */
@Slf4j
@Import({DingtalkConfiguration.class, EmailConfiguration.class})
@AutoConfiguration
public class NotifierAutoConfiguration {

    /**
     * 通知管理器
     */
    @Bean
    public NotifierManager notifierManager(java.util.List<com.wzkris.common.notifier.api.Notifier<?>> notifiers) {
        return new NotifierManager(notifiers);
    }

}
