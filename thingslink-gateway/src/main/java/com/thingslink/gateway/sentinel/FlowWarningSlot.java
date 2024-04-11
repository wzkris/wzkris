package com.thingslink.gateway.sentinel;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleChecker;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

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
        List<FlowRule> rules = getRuleProvider(application);
        // 判断是否feign调用
        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (application.startsWith(httpMethod.name())) {
                // 判断是否feign调用
                return;
            }
        }
        if (CollectionUtils.isEmpty(rules)) {
            log.warn("sentinel warning：服务'{}'未配置流量规则", application);
        }
        else {
            for (FlowRule rule : rules) {
                //这里取到的规则都是配置阈值的80%,这里如果检查到阈值了，说明就是到了真实阈值的80%，既可以发报警给对应负责人了
                if (!checker.canPassCheck(rule, context, node, count, prioritized)) {
                    FlowRule originRule = getOriginRule(application);
                    String originRuleCount = originRule == null ? "UNKNOWN_RULE" : String.valueOf(originRule.getCount());
                    log.warn("sentinel warning：服务’{}‘超过预警指标'{}'，流控阈值'{}',", application, rule.getCount(), originRuleCount);
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

    /**
     * 获取规则
     */
    private List<FlowRule> getRuleProvider(String resource) {
        // Flow rule map should not be null.
        List<FlowRule> rules = FlowRuleManager.getRules();
        List<FlowRule> earlyWarningRuleList = Lists.newArrayList();
        for (FlowRule rule : rules) {
            FlowRule earlyWarningRule = new FlowRule();
            BeanUtil.copyProperties(rule, earlyWarningRule);
            //TODO 这里是相当于把规则阈值改成原来的80%，达到提前预警的效果，建议把0.8做成配置
            earlyWarningRule.setCount(rule.getCount() * 0.8);
            earlyWarningRuleList.add(earlyWarningRule);
        }
        Map<String, List<FlowRule>> flowRules = FlowRuleUtil.buildFlowRuleMap(earlyWarningRuleList);
        return flowRules.get(resource);
    }

    /**
     * get origin rule
     */
    private FlowRule getOriginRule(String resource) {
        List<FlowRule> originRule = FlowRuleManager.getRules().stream().filter(flowRule -> flowRule.getResource().equals(resource)).toList();
        if (CollectionUtils.isEmpty(originRule)) {
            return null;
        }
        return originRule.get(0);
    }
}
