package com.wzkris.gateway.sentinel;

import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleChecker;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 流量告警spi
 * @date : 2023/5/19 13:24
 * @update: 2025/04/26 13:13 优化告警次数
 */
@Slf4j
public class FlowWarningProcessorSlot extends AbstractLinkedProcessorSlot<DefaultNode> {

    private static final long WARNING_INTERVAL_MS = 180_000; // 3分钟警告间隔

    private final FlowRuleChecker checker;

    private final ConcurrentHashMap<String, AtomicLong> lastWarningTimeMap = new ConcurrentHashMap<>();

    public FlowWarningProcessorSlot() {
        this.checker = new FlowRuleChecker();
    }

    @Override
    public void entry(
            Context context,
            ResourceWrapper resourceWrapper,
            DefaultNode node,
            int count,
            boolean prioritized,
            Object... args)
            throws Throwable {
        String resourceName = resourceWrapper.getName();
        List<FlowRule> rules = FlowRuleManager.getRules();

        if (!CollectionUtils.isEmpty(rules)) {
            checkFlowRules(resourceName, context, node, count, prioritized, rules);
        }

        fireEntry(context, resourceWrapper, node, count, prioritized, args);
    }

    private void checkFlowRules(
            String resourceName,
            Context context,
            DefaultNode node,
            int count,
            boolean prioritized,
            List<FlowRule> rules) {
        for (FlowRule rule : rules) {
            if (resourceName.equals(rule.getResource())
                    && !checker.canPassCheck(rule, context, node, count, prioritized)) {

                handleFlowLimit(resourceName, rule);
                break;
            }
        }
    }

    private void handleFlowLimit(String resourceName, FlowRule rule) {
        long currentTime = System.currentTimeMillis();
        AtomicLong lastWarningTime = lastWarningTimeMap.computeIfAbsent(resourceName, k -> new AtomicLong(currentTime));

        long lastTime = lastWarningTime.get();

        // 首次触发（lastTime == currentTime）或超过3分钟间隔
        if (lastTime == currentTime || (currentTime - lastTime) > WARNING_INTERVAL_MS) {

            // CAS更新成功才执行告警（防止并发重复告警）
            if (lastWarningTime.compareAndSet(lastTime, currentTime)) {
                logFlowLimitWarning(resourceName, rule);
                triggerAlarm(resourceName, rule);
            }
        }
    }

    private void logFlowLimitWarning(String resourceName, FlowRule rule) {
        log.warn(
                "[Sentinel Flow Limit] Resource '{}' triggered flow control. "
                        + "Threshold: {}, Strategy: {}, ControlBehavior: {}",
                resourceName,
                rule.getCount(),
                rule.getStrategy(),
                rule.getControlBehavior());
    }

    protected void triggerAlarm(String resourceName, FlowRule rule) {
        // TODO 实现具体的告警逻辑
    }

    @Override
    public void exit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
        fireExit(context, resourceWrapper, count, args);
    }

}
