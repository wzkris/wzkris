package com.wzkris.common.notifier.core;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 通用通知上下文
 * 包含所有可能需要的通知字段，由具体的Notifier实现类根据配置提取需要的参数
 *
 * @author wzkris
 * @date 2026/01/05
 */
@Data
@Builder
public class NotificationContext {

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容（文本内容）
     */
    private String content;

    /**
     * 接收人列表（用于邮件等需要明确接收人的渠道）
     */
    private java.util.List<String> recipients;

    /**
     * Webhook标识（用于钉钉等多webhook场景）
     */
    private String webhookKey;

    /**
     * 扩展字段，用于传递其他自定义参数
     */
    private Map<String, Object> extras;

}

