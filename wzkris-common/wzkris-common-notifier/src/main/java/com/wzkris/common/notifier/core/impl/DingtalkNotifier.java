package com.wzkris.common.notifier.core.impl;

import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.notifier.client.DingtalkMsgClient;
import com.wzkris.common.notifier.core.Notifier;
import com.wzkris.common.notifier.domain.DingtalkMessage;
import com.wzkris.common.notifier.domain.NotificationResult;
import com.wzkris.common.notifier.enums.NotificationChannelEnum;
import com.wzkris.common.notifier.properties.NotifierProperties;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.util.Assert;

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

}
