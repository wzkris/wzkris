package com.wzkris.risk.service.decision;

import com.wzkris.common.core.enums.RiskLevelEnum;
import com.wzkris.risk.domain.RiskDecision;
import com.wzkris.risk.enums.RiskActionEnum;
import com.wzkris.risk.enums.RiskDecisionEnum;

public final class RiskDecisionResponseFactory {

    private RiskDecisionResponseFactory() {
    }

    public static RiskDecision allow() {
        return new RiskDecision(
                RiskDecisionEnum.ALLOW.name(),
                RiskActionEnum.NONE.name(),
                "ok",
                RiskLevelEnum.LOW.getValue(),
                0
        );
    }

    public static RiskDecision block(String reason, int score) {
        return new RiskDecision(
                RiskDecisionEnum.BLOCK.name(),
                RiskActionEnum.SHOW_BLOCK_MODAL.name(),
                reason,
                RiskLevelEnum.HIGH.getValue(),
                score
        );
    }

    public static RiskDecision captcha(String reason, int score) {
        return new RiskDecision(
                RiskDecisionEnum.CAPTCHA.name(),
                RiskActionEnum.SHOW_CAPTCHA_MODAL.name(),
                reason,
                RiskLevelEnum.MEDIUM.getValue(),
                score
        );
    }

}
