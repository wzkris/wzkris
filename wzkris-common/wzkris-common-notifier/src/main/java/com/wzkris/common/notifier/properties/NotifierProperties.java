package com.wzkris.common.notifier.properties;

import com.wzkris.common.notifier.enums.DingtalkTemplateKeyEnum;
import com.wzkris.common.notifier.enums.EmailTemplateKeyEnum;
import com.wzkris.common.notifier.enums.NotificationChannelEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 错误日志通知配置属性
 *
 * @author wzkris
 */
@Slf4j
@Data
@RefreshScope
@ConfigurationProperties(prefix = "notifier")
public class NotifierProperties {

    /**
     * 是否启用错误日志通知
     */
    private Boolean enabled = false;

    /**
     * 发送渠道（启用时必须显式配置，如：DINGTALK, EMAIL）
     */
    private NotificationChannelEnum channel;

    /**
     * 钉钉通知配置
     */
    private DingtalkConfig dingtalk = new DingtalkConfig();

    /**
     * 邮件通知配置
     */
    private EmailConfig email = new EmailConfig();

    @Data
    public static class DingtalkConfig {

        /**
         * 多个机器人Webhook配置
         * Key: webhook标识（如：default, alarm, business等）
         * Value: webhook URL
         */
        private Map<String, String> webhooks;

        /**
         * 模板类型
         */
        private DingtalkTemplateKeyEnum templateKey = DingtalkTemplateKeyEnum.TEXT;

    }

    @Data
    public static class EmailConfig {

        /**
         * 接收人列表（邮箱地址）
         */
        private List<String> recipients = new ArrayList<>();

        /**
         * 发件人名称
         */
        private String fromName = "系统异常通知";

        /**
         * 邮件模板类型（PLAINTEXT 或 HTML，默认 PLAINTEXT）
         */
        private EmailTemplateKeyEnum templateKey = EmailTemplateKeyEnum.PLAINTEXT;

    }

}

