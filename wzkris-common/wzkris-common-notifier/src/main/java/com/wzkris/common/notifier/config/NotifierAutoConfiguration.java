package com.wzkris.common.notifier.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.wzkris.common.notifier.appender.ErrorLogEventAppender;
import com.wzkris.common.notifier.core.Notifier;
import com.wzkris.common.notifier.core.NotifierManager;
import com.wzkris.common.notifier.listener.ErrorNotifierListener;
import com.wzkris.common.notifier.properties.NotifierProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(NotifierProperties.class)
@AutoConfiguration
public class NotifierAutoConfiguration implements InitializingBean {

    /**
     * 通知管理器
     */
    @Bean
    public NotifierManager notifierManager(java.util.List<Notifier<?>> notifiers,
                                           NotifierProperties properties) {
        return new NotifierManager(notifiers, properties);
    }

    /**
     * 错误日志通知监听器
     */
    @Bean
    @ConditionalOnBean(NotifierManager.class)
    public ErrorNotifierListener errorLogNotifierListener(NotifierManager notifierManager,
                                                          NotifierProperties properties) {
        return new ErrorNotifierListener(notifierManager, properties);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);

        rootLogger.addAppender(new ErrorLogEventAppender());
        log.info("已自动配置错误日志事件 Appender");
    }

}
