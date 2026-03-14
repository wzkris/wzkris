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
@Order(40)
@RequiredArgsConstructor
public class PathBurstRule implements RiskRule {

    private final RiskDecisionStateService stateService;

    @Override
    public Optional<RiskDecision> evaluate(RiskDecisionContext context) {
        if (stateService.reachPathBurst(context.getRequestPath(), context.getIpAddr())) {
            return Optional.of(RiskDecisionResponseFactory.block("检测到路径爆破行为，请稍后重试", 88));
        }
        return Optional.empty();
    }

}
