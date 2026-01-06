package com.wzkris.common.notifier.core;

import com.wzkris.common.notifier.enums.NotificationChannelEnum;
import com.wzkris.common.notifier.properties.NotifierProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class NotifierManager {

    private final Map<NotificationChannelEnum, Notifier<?>> notifiers = new HashMap<>();

    private final NotifierProperties properties;

    public NotifierManager(List<Notifier<?>> notifiers, NotifierProperties properties) {
        this.properties = properties;
        Assert.notEmpty(notifiers, "未找到任何通知器实现，请检查配置");
        for (Notifier<?> n : notifiers) {
            NotificationChannelEnum ch = n.getChannel();
            this.notifiers.put(ch, n);
        }
        log.info("已加载 {} 个通知器: {}", this.notifiers.size(), this.notifiers.keySet());
    }

    /**
     * 根据配置的渠道自动构建并发送通知
     * 这是最便捷的方法，调用方只需要提供NotificationContext，系统会根据配置自动选择渠道并构建消息
     *
     * @param context 通知上下文
     * @return 通知结果
     */
    public NotificationResult send(NotificationContext context) {
        Assert.notNull(context, "通知上下文不能为空");
        Assert.notNull(properties.getChannel(), "未配置通知渠道，请设置 notifier.channel");

        NotificationChannelEnum channel = properties.getChannel();
        Notifier<?> notifier = notifiers.get(channel);
        if (notifier == null) {
            return NotificationResult.failure("未找到渠道的通知器: " + channel);
        }

        try {
            // 根据渠道类型构建对应的消息
            Object message = notifier.buildMessage(context);
            if (message == null) {
                return NotificationResult.failure("构建消息失败: 不支持的渠道 " + channel);
            }

            // 发送消息
            Notifier<Object> n = (Notifier<Object>) notifier;
            return n.send(message);
        } catch (Exception e) {
            log.error("发送通知失败", e);
            return NotificationResult.failure(e.getMessage());
        }
    }

}
