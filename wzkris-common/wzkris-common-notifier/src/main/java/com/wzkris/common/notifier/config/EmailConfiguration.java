package com.wzkris.common.notifier.config;

import com.wzkris.common.notifier.core.impl.EmailNotifier;
import com.wzkris.common.notifier.properties.NotifierProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 邮件配置类
 *
 * @author wzkris
 * @date 2025/11/06
 */
@Slf4j
@Configuration
@AutoConfigureAfter()
@ConditionalOnProperty(prefix = "notifier", name = "enabled", havingValue = "true")
public class EmailConfiguration {

    /**
     * 邮件通知器（使用 Spring Boot 自动配置的单实例 JavaMailSender）
     */
    @Bean
    @ConditionalOnProperty("spring.mail.host")
    @ConditionalOnProperty(prefix = "notifier", name = "channel", havingValue = "EMAIL")
    public EmailNotifier emailNotifier(JavaMailSender mailSender, NotifierProperties notifierProperties) {
        log.info("启用基于 Spring Boot 自动配置的邮件通知器");
        return new EmailNotifier(mailSender, notifierProperties);
    }

}
