package com.wzkris.risk.service.decision.rule;

import com.wzkris.risk.domain.RiskDecision;
import com.wzkris.risk.service.decision.RiskDecisionContext;
import com.wzkris.risk.service.decision.RiskDecisionResponseFactory;
import com.wzkris.risk.service.decision.RiskDecisionStateService;
import com.wzkris.risk.service.decision.RiskRule;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(30)
@RequiredArgsConstructor
public class IpLimitRule implements RiskRule {

    private final RiskDecisionStateService stateService;

    @Override
    public Optional<RiskDecision> evaluate(RiskDecisionContext context) {
        if (stateService.reachIpLimit(context.getIpAddr())) {
            return Optional.of(RiskDecisionResponseFactory.block("请求频率过高，请稍后重试", 85));
        }
        return Optional.empty();
    }

}
