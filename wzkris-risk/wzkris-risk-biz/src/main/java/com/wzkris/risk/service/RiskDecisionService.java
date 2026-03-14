package com.wzkris.risk.service;

import com.wzkris.risk.domain.RiskDecision;
import com.wzkris.risk.enums.RiskDecisionEnum;
import com.wzkris.risk.httpclient.riskctl.req.RiskDecideReq;
import com.wzkris.risk.httpclient.riskctl.req.RiskReportReq;
import com.wzkris.risk.service.decision.RiskDecisionContext;
import com.wzkris.risk.service.decision.RiskDecisionEngine;
import com.wzkris.risk.service.decision.RiskDecisionResponseFactory;
import com.wzkris.risk.service.decision.RiskDecisionStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RiskDecisionService {

    private final RiskDecisionEngine decisionEngine;

    private final RiskDecisionStateService stateService;

    public RiskDecision decide(RiskDecideReq request) {
        RiskDecisionContext context = new RiskDecisionContext(request);
        Optional<RiskDecision> decision = decisionEngine.decide(context);
        if (decision.isPresent()) {
            RiskDecision resp = decision.get();
            // 如果是允许访问，则更新用户的IP和UA记录
            if (RiskDecisionEnum.ALLOW.name().equals(resp.getDecision())) {
                updateUserState(request);
            }
            return resp;
        }
        // 默认允许访问，更新用户状态
        updateUserState(request);
        return RiskDecisionResponseFactory.allow();
    }

    private void updateUserState(RiskDecideReq request) {
        stateService.updateLastIp(request.getAuthType(), request.getSubject(), request.getIpAddr());
        stateService.updateLastUa(request.getAuthType(), request.getSubject(), request.getUserAgent());
    }

    public void report(RiskReportReq request) {
        if (Boolean.TRUE.equals(request.getSuccess())) {
            stateService.clearFailCount(request.getAuthType(), request.getSubject());
            return;
        }
        stateService.incrementFailCount(request.getAuthType(), request.getSubject());
    }

}
