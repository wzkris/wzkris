package com.wzkris.common.notifier.core.impl;

import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.notifier.client.DingtalkMsgClient;
import com.wzkris.common.notifier.core.NotificationContext;
import com.wzkris.common.notifier.core.NotificationResult;
import com.wzkris.common.notifier.core.Notifier;
import com.wzkris.common.notifier.domain.DingtalkMessage;
import com.wzkris.common.notifier.enums.NotificationChannelEnum;
import com.wzkris.common.notifier.properties.NotifierProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 钉钉通知器实现
 * 负责将 NotificationMessage 适配为钉钉 API 调用
 * 支持多个webhook，根据消息中的webhookKey选择对应的webhook
 *
 * @author wzkris
 * @date 2025/11/06
 */
@Slf4j
public class DingtalkNotifier implements Notifier<DingtalkMessage> {

    private final DingtalkMsgClient dingtalkMsgClient;

    private final NotifierProperties notifierProperties;

    public DingtalkNotifier(DingtalkMsgClient dingtalkMsgClient, NotifierProperties notifierProperties) {
        this.dingtalkMsgClient = dingtalkMsgClient;
        this.notifierProperties = notifierProperties;
    }

    @Override
    public NotificationResult send(DingtalkMessage message) {
        try {
            final String webhookUrl = getWebhookUrl(message.getWebhookKey());
            Assert.notNull(webhookUrl, "获取不到钉钉推送url, webHookKey: " + message.getWebhookKey() + " ,请检查配置: notifier.dingtalk.webhooks");

            String result = dingtalkMsgClient.sendGroupMessage(
                    webhookUrl,
                    message.getTemplateKey().value(),
                    JsonUtil.toJsonString(message.getTemplateParams())
            );

            return NotificationResult.success(result);
        } catch (Exception e) {
            return NotificationResult.failure(e.getMessage());
        }
    }

    /**
     * 根据webhookKey获取webhook URL
     *
     * @param webhookKey webhook标识
     * @return webhook URL
     */
    @Nullable
    private String getWebhookUrl(String webhookKey) {
        NotifierProperties.DingtalkConfig dingtalkConfig = notifierProperties.getDingtalk();
        Map<String, String> webhooks = dingtalkConfig.getWebhooks();
        if (MapUtils.isEmpty(webhooks)) return null;

        return webhooks.get(webhookKey);
    }

    @Override
    public NotificationChannelEnum getChannel() {
        return NotificationChannelEnum.DINGTALK;
    }

    @Override
    public DingtalkMessage buildMessage(NotificationContext context) {
        Assert.notNull(context, "通知上下文不能为空");

        NotifierProperties.DingtalkConfig dingtalkConfig = notifierProperties.getDingtalk();
        Assert.notNull(dingtalkConfig, "钉钉配置不能为空");

        // 构建模板参数
        Map<String, Object> templateParams = buildTemplateParams(context);

        return DingtalkMessage.builder()
                .templateKey(dingtalkConfig.getTemplateKey())
                .templateParams(templateParams)
                .webhookKey(context.getWebhookKey())
                .build();
    }

    /**
     * 根据模板类型和context构建模板参数
     */
    private Map<String, Object> buildTemplateParams(NotificationContext context) {
        Map<String, Object> params = new HashMap<>();

        switch (notifierProperties.getDingtalk().getTemplateKey()) {
            case TEXT:
                // TEXT模板只需要content
                params.put("content", context.getContent());
                break;
            case MARKDOWN:
                // MARKDOWN模板需要title和text
                params.put("title", context.getTitle());
                params.put("text", context.getContent());
                break;
            case LINK:
                // LINK模板需要text, title, picUrl, messageUrl
                params.put("title", context.getTitle());
                params.put("text", context.getContent());
                // picUrl和messageUrl可以从extras中获取
                if (MapUtils.isNotEmpty(context.getExtras())) {
                    Object picUrl = context.getExtras().get("picUrl");
                    Object messageUrl = context.getExtras().get("messageUrl");
                    if (picUrl != null) {
                        params.put("picUrl", picUrl);
                    }
                    if (messageUrl != null) {
                        params.put("messageUrl", messageUrl);
                    }
                }
                break;
            case ACTION_CARD:
                // ACTION_CARD模板需要title, text, singleTitle, singleURL
                params.put("title", context.getTitle());
                params.put("text", context.getContent());
                // singleTitle和singleURL可以从extras中获取
                if (MapUtils.isNotEmpty(context.getExtras())) {
                    Object singleTitle = context.getExtras().get("singleTitle");
                    Object singleURL = context.getExtras().get("singleURL");
                    if (singleTitle != null) {
                        params.put("singleTitle", singleTitle);
                    }
                    if (singleURL != null) {
                        params.put("singleURL", singleURL);
                    }
                }
                break;
            default:
                break;
        }

        return params;
    }

}
