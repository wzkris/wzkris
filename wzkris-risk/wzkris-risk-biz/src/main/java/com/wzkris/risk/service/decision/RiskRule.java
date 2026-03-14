package com.wzkris.risk.service.decision;

import com.wzkris.risk.domain.RiskDecision;

import java.util.Optional;

public interface RiskRule {

    Optional<RiskDecision> evaluate(RiskDecisionContext context);

}
