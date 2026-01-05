package com.wzkris.common.notifier.core.impl;

import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.notifier.client.DingtalkMsgClient;
import com.wzkris.common.notifier.core.Notifier;
import com.wzkris.common.notifier.domain.DingtalkMessage;
import com.wzkris.common.notifier.domain.NotificationResult;
import com.wzkris.common.notifier.enums.NotificationChannelEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * 钉钉通知器实现
 * 负责将 NotificationMessage 适配为钉钉 API 调用
 *
 * @author wzkris
 * @date 2025/11/06
 */
@Slf4j
public class DingtalkNotifier implements Notifier<DingtalkMessage> {

    private final DingtalkMsgClient dingtalkMsgClient;

    public DingtalkNotifier(DingtalkMsgClient dingtalkMsgClient) {
        Assert.notNull(dingtalkMsgClient, "钉钉API客户端不能为空");
        this.dingtalkMsgClient = dingtalkMsgClient;
    }

    @Override
    public NotificationResult send(DingtalkMessage message) {
        try {
            // 发送群消息
            String result = dingtalkMsgClient.sendGroupMessage(
                    message.getTemplateKey().value(),
                    JsonUtil.toJsonString(message.getTemplateParams())
            );

            return NotificationResult.success(result);
        } catch (Exception e) {
            log.error("发送钉钉消息失败", e);
            return NotificationResult.failure(e.getMessage());
        }
    }

    @Override
    public NotificationChannelEnum getChannel() {
        return NotificationChannelEnum.DINGTALK;
    }

}
