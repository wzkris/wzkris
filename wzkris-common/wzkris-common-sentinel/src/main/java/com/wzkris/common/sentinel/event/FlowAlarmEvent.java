package com.wzkris.common.sentinel.event;

import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;

/**
 * 限流告警事件
 *
 * @param resourceName 资源名称
 * @param rule         限流规则
 * @author wzkris
 * @date 2025/12/30
 */
public record FlowAlarmEvent(String resourceName, FlowRule rule) {

}

