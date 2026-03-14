package com.wzkris.risk.service.decision;

import com.wzkris.risk.domain.RiskDecision;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 风险决策引擎，负责按顺序执行规则并返回第一个命中的决策。
 */
@Service
@RequiredArgsConstructor
public class RiskDecisionEngine {

    private final List<RiskRule> rules;

    public Optional<RiskDecision> decide(RiskDecisionContext context) {
        for (RiskRule rule : rules) {
            Optional<RiskDecision> decision = rule.evaluate(context);
            if (decision.isPresent()) {
                return decision;
            }
        }
        return Optional.empty();
    }

}

