package com.wzkris.common.notifier.listener;

import com.wzkris.common.notifier.core.NotifierManager;
import com.wzkris.common.notifier.event.ErrorLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

/**
 * 错误日志通知监听器
 * 监听 ErrorLogEvent 事件并发送通知
 *
 * @author wzkris
 */
@Slf4j
public class ErrorLogNotifierListener {

    private final NotifierManager notifierManager;

    public ErrorLogNotifierListener(NotifierManager notifierManager) {
        this.notifierManager = notifierManager;
    }

    /**
     * 监听错误日志事件并发送通知
     * 注意：此方法在日志 Appender 线程中执行，应保持快速处理，避免阻塞日志输出
     */
    @EventListener
    public void onErrorLogEvent(ErrorLogEvent event) {
        try {
            // 获取原始消息（控制台打印的完整信息）
            String originalMessage = event.getOriginalMessage();
            log.info("错误日志通知:\n{}", originalMessage);

        } catch (Exception e) {
            // 避免通知失败影响主流程
            log.error("处理错误日志通知失败", e);
        }
    }

}
