package com.wzkris.payment.job;

import com.wzkris.payment.domain.NotifyTaskDO;
import com.wzkris.payment.service.NotifyTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 业务方通知重试任务：扫到期 PENDING 任务重投
 *
 * <p>P0 用 @Scheduled（WebAutoConfiguration 已 @EnableScheduling）；P6 切 XXL-Job 分布式调度
 *
 * @author wzkris
 */
@Component
@RequiredArgsConstructor
public class NotifyRetryJob {

    private final NotifyTaskService notifyTaskService;

    @Scheduled(fixedDelay = 30_000L)
    public void retry() {
        List<NotifyTaskDO> tasks = notifyTaskService.findPendingRetryTasks(100);
        for (NotifyTaskDO task : tasks) {
            try {
                notifyTaskService.send(task);
            } catch (Exception ignored) {
                // 单条失败不影响其他任务
            }
        }
    }

}
