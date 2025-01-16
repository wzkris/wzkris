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

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 流量告警spi
 * @date : 2023/5/19 13:24
 */
@Slf4j
public class FlowWarningSlot extends AbstractLinkedProcessorSlot<DefaultNode> {

    private final FlowRuleChecker checker;

    public FlowWarningSlot() {
        this.checker = new FlowRuleChecker();
    }

    @Override
    public void entry(Context context, ResourceWrapper resourceWrapper, DefaultNode node, int count, boolean prioritized, Object... args) throws Throwable {
        String application = context.getCurEntry().getResourceWrapper().getName();
        List<FlowRule> rules = FlowRuleManager.getRules();
        if (!CollectionUtils.isEmpty(rules)) {
            for (FlowRule rule : rules) {
                if (application.equals(rule.getResource()) &&
                        !checker.canPassCheck(rule, context, node, count, prioritized)) {
                    log.error("sentinel warning：服务’{}‘触发限流，流控阈值'{}',", application, rule.getCount());
                    //TODO 报警功能自行实现
                    break;
                }
            }
        }
        fireEntry(context, resourceWrapper, node, count, prioritized, args);
    }

    @Override
    public void exit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
        fireExit(context, resourceWrapper, count, args);
    }
}
