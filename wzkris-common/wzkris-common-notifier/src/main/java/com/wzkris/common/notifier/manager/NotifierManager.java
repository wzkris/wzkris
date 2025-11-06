package com.wzkris.common.notifier.manager;

import com.wzkris.common.notifier.api.Notifier;
import com.wzkris.common.notifier.domain.NotificationResult;
import com.wzkris.common.notifier.enums.NotificationChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class NotifierManager {

    private final Map<NotificationChannel, Notifier<?>> notifiers = new HashMap<>();

    public NotifierManager(List<Notifier<?>> notifiers) {
        if (notifiers == null || notifiers.isEmpty()) {
            log.warn("未找到任何通知器实现");
            return;
        }
        for (Notifier<?> n : notifiers) {
            NotificationChannel ch = n.getChannel();
            if (this.notifiers.containsKey(ch)) {
                log.warn("发现重复的通知器: {}，使用第一个", ch);
                continue;
            }
            this.notifiers.put(ch, n);
        }
        log.info("已加载 {} 个通知器: {}", this.notifiers.size(), this.notifiers.keySet());
    }

    public NotificationResult send(NotificationChannel channel, Object message) {
        Assert.notNull(channel, "通知渠道不能为空");
        Assert.notNull(message, "通知消息不能为空");
        Notifier<?> notifier = notifiers.get(channel);
        if (notifier == null) {
            return NotificationResult.failure("未找到渠道的通知器: " + channel);
        }
        try {
            Notifier<Object> n = (Notifier<Object>) notifier;
            return n.send(message);
        } catch (Exception e) {
            log.error("发送通知失败", e);
            return NotificationResult.failure(e.getMessage());
        }
    }

}
