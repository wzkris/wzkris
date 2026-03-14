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
@Order(60)
@RequiredArgsConstructor
public class BehaviorScoreRule implements RiskRule {

    private final RiskDecisionStateService stateService;

    @Override
    public Optional<RiskDecision> evaluate(RiskDecisionContext context) {
        int score = 0;
        if (stateService.isIpDrift(context.getAuthType(), context.getSubject(), context.getIpAddr())) {
            score += 35;
        }
        if (stateService.isUaMutation(context.getAuthType(), context.getSubject(), context.getUserAgent())) {
            score += 30;
        }
        if (score >= 60) {
            return Optional.of(RiskDecisionResponseFactory.captcha("环境变化异常，请完成安全验证", score));
        }
        return Optional.empty();
    }

}
