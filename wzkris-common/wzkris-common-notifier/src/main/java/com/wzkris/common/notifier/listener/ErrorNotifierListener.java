package com.wzkris.common.notifier.listener;

import com.wzkris.common.notifier.core.NotifierManager;
import com.wzkris.common.notifier.domain.DingtalkMessage;
import com.wzkris.common.notifier.domain.EmailMessage;
import com.wzkris.common.notifier.domain.NotificationResult;
import com.wzkris.common.notifier.enums.DingtalkTemplateKeyEnum;
import com.wzkris.common.notifier.enums.EmailTemplateKeyEnum;
import com.wzkris.common.notifier.enums.NotificationChannelEnum;
import com.wzkris.common.notifier.event.ErrorLogEvent;
import com.wzkris.common.notifier.properties.NotifierProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常通知监听器
 * 监听 ErrorLogEvent 事件并发送通知
 *
 * @author wzkris
 */
@Slf4j
public class ErrorNotifierListener {

    private final NotifierManager notifierManager;

    private final NotifierProperties properties;

    public ErrorNotifierListener(NotifierManager notifierManager, NotifierProperties properties) {
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
            log.warn("错误日志通知已启用，但未配置发送渠道，跳过通知。请配置 notifier.channel");
            return;
        }

        try {
            sendNotification(channel, event);
        } catch (Exception e) {
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
        NotifierProperties.DingtalkConfig dingtalkConfig = properties.getDingtalk();
        if (dingtalkConfig == null) {
            log.warn("钉钉通知未配置接收人，跳过发送");
            return;
        }

        // 解析模板类型
        DingtalkTemplateKeyEnum templateKey = dingtalkConfig.getTemplateKey();

        // 构建消息参数
        Map<String, Object> templateParams = new HashMap<>();
        if (templateKey == DingtalkTemplateKeyEnum.MARKDOWN) {
            templateParams.put("title", "系统异常通知");
            templateParams.put("text", event.getOriginalMessage());
        } else if (templateKey == DingtalkTemplateKeyEnum.TEXT) {
            templateParams.put("content", event.getOriginalMessage());
        } else {
            log.warn("错误日志通知暂不支持钉钉模板类型: {}", templateKey);
            return;
        }

        // 构建消息
        DingtalkMessage message = DingtalkMessage.builder()
                .templateKey(templateKey)
                .templateParams(templateParams)
                .webhookKey("error-log")
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
        NotifierProperties.EmailConfig emailConfig = properties.getEmail();
        if (emailConfig == null || CollectionUtils.isEmpty(emailConfig.getRecipients())) {
            return;
        }

        // 解析模板类型
        EmailTemplateKeyEnum templateKey = emailConfig.getTemplateKey();

        // 构建消息
        EmailMessage message = EmailMessage.builder()
                .templateKey(templateKey)
                .recipients(emailConfig.getRecipients())
                .subject("系统异常通知")
                .content(event.getOriginalMessage())
                .fromEmail(emailConfig.getFromEmail())
                .fromName(emailConfig.getFromName())
                .build();

        // 发送通知
        NotificationResult result = notifierManager.send(NotificationChannelEnum.EMAIL, message);
        if (!result.getSuccess()) {
            log.warn("错误日志邮件通知发送失败: {}", result.getErrorMessage());
        }
    }

}
