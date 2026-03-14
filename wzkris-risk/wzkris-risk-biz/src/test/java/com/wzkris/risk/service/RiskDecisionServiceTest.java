package com.wzkris.risk.service;

import com.wzkris.common.core.enums.RiskLevelEnum;
import com.wzkris.risk.domain.RiskDecision;
import com.wzkris.risk.httpclient.riskctl.req.RiskDecideReq;
import com.wzkris.risk.httpclient.riskctl.req.RiskReportReq;
import com.wzkris.risk.service.decision.RiskDecisionContext;
import com.wzkris.risk.service.decision.RiskDecisionEngine;
import com.wzkris.risk.service.decision.RiskDecisionStateService;
import com.wzkris.risk.service.decision.RiskRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RiskDecisionServiceTest {

    @Mock
    private RiskRule firstRule;

    @Mock
    private RiskRule secondRule;

    @Mock
    private RiskDecisionStateService stateService;

    private RiskDecisionService riskDecisionService;

    @BeforeEach
    void setUp() {
        riskDecisionService = new RiskDecisionService(new RiskDecisionEngine(List.of(firstRule, secondRule)), stateService);
    }

    @Test
    void decide_shouldReturnFirstMatchedRuleResult() {
        RiskDecideReq req = new RiskDecideReq();
        RiskDecision expected = new RiskDecision("BLOCK", "SHOW_BLOCK_MODAL", "blocked", RiskLevelEnum.HIGH.getValue(), 80);
        when(firstRule.evaluate(org.mockito.ArgumentMatchers.any(RiskDecisionContext.class))).thenReturn(Optional.empty());
        when(secondRule.evaluate(org.mockito.ArgumentMatchers.any(RiskDecisionContext.class))).thenReturn(Optional.of(expected));

        RiskDecision decide = riskDecisionService.decide(req);
        assertNotNull(decide);
        assertEquals("BLOCK", decide.getDecision());
        assertEquals("SHOW_BLOCK_MODAL", decide.getAction());
    }

    @Test
    void decide_shouldAllowWhenNoRuleMatched() {
        RiskDecideReq req = new RiskDecideReq();
        when(firstRule.evaluate(org.mockito.ArgumentMatchers.any(RiskDecisionContext.class))).thenReturn(Optional.empty());
        when(secondRule.evaluate(org.mockito.ArgumentMatchers.any(RiskDecisionContext.class))).thenReturn(Optional.empty());

        RiskDecision resp = riskDecisionService.decide(req);
        assertNotNull(resp);
        assertEquals("ALLOW", resp.getDecision());
        assertEquals("NONE", resp.getAction());
    }

    @Test
    void report_shouldClearFailCounterOnSuccess() {
        RiskReportReq req = new RiskReportReq();
        req.setAuthType("admin");
        req.setSubject("root");
        req.setSuccess(true);

        riskDecisionService.report(req);

        verify(stateService).clearFailCount("admin", "root");
    }

    @Test
    void report_shouldIncreaseFailCounterOnFailure() {
        RiskReportReq req = new RiskReportReq();
        req.setAuthType("admin");
        req.setSubject("root");
        req.setSuccess(false);

        riskDecisionService.report(req);

        verify(stateService).incrementFailCount("admin", "root");
    }

}
