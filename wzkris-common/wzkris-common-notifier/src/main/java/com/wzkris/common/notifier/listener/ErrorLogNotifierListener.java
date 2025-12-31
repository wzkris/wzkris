package com.wzkris.common.notifier.listener;

import com.wzkris.common.notifier.core.NotifierManager;
import com.wzkris.common.notifier.domain.DingtalkMessage;
import com.wzkris.common.notifier.domain.EmailMessage;
import com.wzkris.common.notifier.domain.NotificationResult;
import com.wzkris.common.notifier.enums.DingtalkTemplateKeyEnum;
import com.wzkris.common.notifier.enums.EmailTemplateKeyEnum;
import com.wzkris.common.notifier.enums.NotificationChannelEnum;
import com.wzkris.common.notifier.event.ErrorLogEvent;
import com.wzkris.common.notifier.properties.ErrorLogNotifierProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 错误日志通知监听器
 * 监听 ErrorLogEvent 事件并发送通知
 *
 * @author wzkris
 */
@Slf4j
public class ErrorLogNotifierListener {

    private final NotifierManager notifierManager;

    private final ErrorLogNotifierProperties properties;

    public ErrorLogNotifierListener(NotifierManager notifierManager, ErrorLogNotifierProperties properties) {
        this.notifierManager = notifierManager;
        this.properties = properties;
    }

    /**
     * 监听错误日志事件并发送通知
     * 注意：此方法在日志 Appender 线程中执行，应保持快速处理，避免阻塞日志输出
     */
    @EventListener
    public void onErrorLogEvent(ErrorLogEvent event) {
        // 检查是否启用
        if (!properties.getEnabled()) {
            return;
        }

        // 检查是否配置了发送渠道（启用时必须配置）
        NotificationChannelEnum channel = properties.getChannel();
        if (channel == null) {
            log.warn("错误日志通知已启用，但未配置发送渠道，跳过通知。请配置 wzkris.notifier.error-log.channel");
            return;
        }

        // 根据配置的渠道发送通知
        try {
            sendNotification(channel, event);
        } catch (Exception e) {
            // ErrorLogEventAppender 已过滤通知相关的 Logger，不会导致循环
            log.error("通过渠道 {} 发送错误日志通知失败", channel, e);
        }
    }

    /**
     * 根据渠道发送通知
     */
    private void sendNotification(NotificationChannelEnum channel, ErrorLogEvent event) {
        switch (channel) {
            case DINGTALK:
                sendDingtalkNotification(event);
                break;
            case EMAIL:
                sendEmailNotification(event);
                break;
            default:
                log.warn("不支持的错误日志通知渠道: {}", channel);
        }
    }

    /**
     * 发送钉钉通知
     */
    private void sendDingtalkNotification(ErrorLogEvent event) {
        ErrorLogNotifierProperties.DingtalkConfig dingtalkConfig = properties.getDingtalk();
        if (dingtalkConfig == null || CollectionUtils.isEmpty(dingtalkConfig.getRecipients())) {
            log.debug("钉钉通知未配置接收人，跳过发送");
            return;
        }

        // 解析模板类型
        DingtalkTemplateKeyEnum templateKey = dingtalkConfig.getTemplateKey();

        // 构建消息参数
        Map<String, Object> templateParams = new HashMap<>();
        if (templateKey == DingtalkTemplateKeyEnum.MARKDOWN) {
            templateParams.put("title", buildDingtalkTitle(event));
            templateParams.put("text", buildDingtalkContent(event));
        } else if (templateKey == DingtalkTemplateKeyEnum.TEXT) {
            templateParams.put("content", buildDingtalkContent(event));
        } else {
            log.warn("错误日志通知暂不支持钉钉模板类型: {}", templateKey);
            return;
        }

        // 构建消息
        DingtalkMessage message = DingtalkMessage.builder()
                .templateKey(templateKey)
                .recipients(dingtalkConfig.getRecipients())
                .templateParams(templateParams)
                .build();

        // 发送通知
        NotificationResult result = notifierManager.send(NotificationChannelEnum.DINGTALK, message);
        if (!result.getSuccess()) {
            log.warn("错误日志钉钉通知发送失败: {}", result.getErrorMessage());
        }
    }

    /**
     * 发送邮件通知
     */
    private void sendEmailNotification(ErrorLogEvent event) {
        ErrorLogNotifierProperties.EmailConfig emailConfig = properties.getEmail();
        if (emailConfig == null || CollectionUtils.isEmpty(emailConfig.getRecipients())) {
            return;
        }

        // 解析模板类型
        EmailTemplateKeyEnum templateKey = emailConfig.getTemplateKey();

        // 构建邮件主题（支持占位符替换）
        String subject = buildEmailSubject(event, event.getLoggerName());

        // 构建邮件内容
        String content = buildEmailContent(event);

        // 构建消息
        EmailMessage message = EmailMessage.builder()
                .templateKey(templateKey)
                .recipients(emailConfig.getRecipients())
                .subject(subject)
                .content(content)
                .fromEmail(emailConfig.getFromEmail())
                .fromName(emailConfig.getFromName())
                .build();

        // 发送通知
        NotificationResult result = notifierManager.send(NotificationChannelEnum.EMAIL, message);
        if (!result.getSuccess()) {
            log.warn("错误日志邮件通知发送失败: {}", result.getErrorMessage());
        }
    }

    /**
     * 构建钉钉通知标题
     */
    private String buildDingtalkTitle(ErrorLogEvent event) {
        return String.format("系统错误通知 - %s", event.getLoggerName());
    }

    /**
     * 构建钉钉通知内容
     */
    private String buildDingtalkContent(ErrorLogEvent event) {
        return "**时间**: " + formatTimestamp(event.getTimestamp()) + "\n" +
                "**级别**: " + event.getLevel() + "\n" +
                "**线程**: " + event.getThreadName() + "\n" +
                "**Logger**: " + event.getLoggerName() + "\n" +
                "**消息**: " + event.getFormattedMessage() + "\n" +
                "\n**完整日志**:\n```\n" + event.getOriginalMessage() + "\n```";
    }

    /**
     * 构建邮件主题（支持占位符）
     */
    private String buildEmailSubject(ErrorLogEvent event, String template) {
        if (!StringUtils.hasText(template)) {
            return "系统错误通知";
        }
        return template
                .replace("{loggerName}", event.getLoggerName())
                .replace("{level}", event.getLevel().toString())
                .replace("{timestamp}", formatTimestamp(event.getTimestamp()));
    }

    /**
     * 构建邮件内容
     */
    private String buildEmailContent(ErrorLogEvent event) {
        return "时间: " + formatTimestamp(event.getTimestamp()) + "\n" +
                "级别: " + event.getLevel() + "\n" +
                "线程: " + event.getThreadName() + "\n" +
                "Logger: " + event.getLoggerName() + "\n" +
                "消息: " + event.getFormattedMessage() + "\n" +
                "\n完整日志:\n" + event.getOriginalMessage();
    }

    /**
     * 格式化时间戳
     */
    private String formatTimestamp(long timestamp) {
        return LocalDateTime.ofEpochSecond(timestamp / 1000, 0, java.time.ZoneOffset.of("+8"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
