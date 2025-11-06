package com.wzkris.common.notifier.impl;

import com.wzkris.common.notifier.api.Notifier;
import com.wzkris.common.notifier.domain.EmailMessage;
import com.wzkris.common.notifier.domain.NotificationResult;
import com.wzkris.common.notifier.enums.EmailTemplateKey;
import com.wzkris.common.notifier.enums.NotificationChannel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 邮件通知器实现
 * 负责将 NotificationMessage 适配为邮件发送
 *
 * @author wzkris
 * @date 2025/11/06
 */
@Slf4j
public class EmailNotifier implements Notifier<EmailMessage> {

    private final JavaMailSender mailSender;

    public EmailNotifier(JavaMailSender mailSender) {
        Assert.notNull(mailSender, "邮件发送器不能为空");
        this.mailSender = mailSender;
    }

    @Override
    public NotificationResult send(EmailMessage message) {
        // 获取接收人列表
        List<String> recipients = message.getRecipients();
        if (CollectionUtils.isEmpty(recipients)) {
            return NotificationResult.failure("邮件接收人不能为空");
        }

        if (Objects.equals(message.getTemplateKey(), EmailTemplateKey.PLAINTEXT)) {
            // 发送纯文本邮件
            return sendTextEmail(message);
        } else if (Objects.equals(message.getTemplateKey(), EmailTemplateKey.HTML)) {
            // 发送HTML格式邮件
            return sendHtmlEmail(message);
        } else {
            return NotificationResult.failure("发送邮件失败: 不支持的邮件模板类型");
        }
    }

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.EMAIL;
    }

    /**
     * 发送纯文本邮件
     */
    private NotificationResult sendTextEmail(EmailMessage message) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(message.getFromName() + " <" + message.getFromEmail() + ">");
            simpleMailMessage.setTo(message.getRecipients().toArray(new String[0]));
            simpleMailMessage.setSubject(message.getSubject());
            simpleMailMessage.setText(message.getContent());

            mailSender.send(simpleMailMessage);

            String messageId = "email-" + System.currentTimeMillis();
            return NotificationResult.success(messageId);
        } catch (Exception e) {
            log.error("发送纯文本邮件失败", e);
            throw new RuntimeException("发送邮件失败: " + e.getMessage(), e);
        }
    }

    /**
     * 发送HTML格式邮件
     */
    private NotificationResult sendHtmlEmail(EmailMessage message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(message.getFromName() + " <" + message.getFromEmail() + ">");
            helper.setTo(message.getRecipients().toArray(new String[0]));
            helper.setSubject(message.getSubject());
            helper.setText(message.getContent(), true);

            mailSender.send(mimeMessage);

            String messageId = "email-" + System.currentTimeMillis();
            return NotificationResult.success(messageId);
        } catch (MessagingException e) {
            log.error("发送HTML邮件失败", e);
            throw new RuntimeException("发送邮件失败: " + e.getMessage(), e);
        }
    }

}
