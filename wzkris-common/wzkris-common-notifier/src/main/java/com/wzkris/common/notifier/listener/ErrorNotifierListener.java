package com.wzkris.common.notifier.listener;

import com.wzkris.common.notifier.core.NotificationContext;
import com.wzkris.common.notifier.core.NotificationResult;
import com.wzkris.common.notifier.core.NotifierManager;
import com.wzkris.common.notifier.enums.NotificationChannelEnum;
import com.wzkris.common.notifier.event.ErrorLogEvent;
import com.wzkris.common.notifier.properties.NotifierProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

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
            // 构建通知上下文
            NotificationContext context = NotificationContext.builder()
                    .title("异常日志告警")
                    .content(event.getOriginalMessage())
                    .webhookKey("error-log")
                    .build();

            NotificationResult result = notifierManager.send(context);

            if (!result.getSuccess()) {
                log.error("通过渠道 {} 发送错误日志通知失败, {}", channel, result);
            }
        } catch (Exception e) {
            log.error("通过渠道 {} 发送错误日志通知失败", channel, e);
        }
    }

}
