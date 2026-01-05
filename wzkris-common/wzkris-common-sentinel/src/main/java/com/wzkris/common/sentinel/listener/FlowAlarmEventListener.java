package com.wzkris.common.sentinel.listener;

import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.wzkris.common.notifier.core.NotifierManager;
import com.wzkris.common.notifier.core.NotificationContext;
import com.wzkris.common.notifier.core.NotificationResult;
import com.wzkris.common.sentinel.event.FlowAlarmEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;

/**
 * 限流告警事件监听器
 *
 * @author wzkris
 * @date 2025/12/30
 */
@Slf4j
public class FlowAlarmEventListener implements EnvironmentAware {

    private final NotifierManager notifierManager;

    private Environment environment;

    public FlowAlarmEventListener(NotifierManager notifierManager) {
        this.notifierManager = notifierManager;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * 处理限流告警事件
     *
     * @param event 限流告警事件
     */
    @EventListener
    public void onFlowAlarmEvent(FlowAlarmEvent event) {
        String resourceName = event.resourceName();
        FlowRule rule = event.rule();

        try {
            sendAlarmNotification(resourceName, rule);
        } catch (Exception e) {
            log.error("处理限流告警事件失败: resource={}", resourceName, e);
        }
    }

    /**
     * 发送告警通知（根据配置的渠道自动选择）
     */
    private void sendAlarmNotification(String resourceName, FlowRule rule) {
        try {
            String appName = getApplicationName();
            String strategyDesc = getStrategyDescription(rule.getStrategy());
            String controlBehaviorDesc = getControlBehaviorDescription(rule.getControlBehavior());

            String title = "🚨 Sentinel 限流告警";
            String content = String.format(
                    """
                            ## %s
                            **应用名称**: %s
                            **资源名称**: `%s`
                            **限流阈值**: %.2f QPS
                            **限流策略**: %s
                            **控制行为**: %s
                            **触发时间**: %s
                            ---
                            > 请及时关注系统流量情况，必要时调整限流规则。""",
                    title,
                    appName,
                    resourceName,
                    rule.getCount(),
                    strategyDesc,
                    controlBehaviorDesc,
                    LocalDateTime.now()
            );

            // 构建通知上下文
            NotificationContext context = NotificationContext.builder()
                    .title(title)
                    .content(content)
                    .webhookKey("alarm")
                    .build();

            // 根据配置的渠道自动构建并发送通知
            NotificationResult result = notifierManager.send(context);

            if (!result.getSuccess()) {
                log.warn("限流告警发送失败: resource={}, error={}", resourceName, result.getErrorMessage());
            }
        } catch (Exception e) {
            log.error("发送告警通知失败: resource={}", resourceName, e);
        }
    }

    /**
     * 获取应用名称
     */
    private String getApplicationName() {
        return environment.getProperty("spring.application.name", "unknown-application");
    }

    /**
     * 获取限流策略描述
     */
    private String getStrategyDescription(int strategy) {
        return switch (strategy) {
            case 0 -> "直接限流";
            case 1 -> "关联限流";
            case 2 -> "链路限流";
            default -> "未知策略(" + strategy + ")";
        };
    }

    /**
     * 获取控制行为描述
     */
    private String getControlBehaviorDescription(int controlBehavior) {
        return switch (controlBehavior) {
            case 0 -> "快速失败";
            case 1 -> "Warm Up";
            case 2 -> "匀速排队";
            default -> "未知行为(" + controlBehavior + ")";
        };
    }

}

