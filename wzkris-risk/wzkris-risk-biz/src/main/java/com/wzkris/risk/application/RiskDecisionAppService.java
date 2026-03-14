package com.wzkris.risk.application;

import com.wzkris.risk.domain.RiskDecision;
import com.wzkris.risk.httpclient.riskctl.req.RiskDecideReq;
import com.wzkris.risk.httpclient.riskctl.req.RiskReportReq;
import com.wzkris.risk.service.RiskDecisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 风险决策应用服务，负责编排 HTTP 请求与领域服务之间的交互。
 */
@Service
@RequiredArgsConstructor
public class RiskDecisionAppService {

    private final RiskDecisionService riskDecisionService;

    public RiskDecision decide(RiskDecideReq request) {
        // 暂时直接委托给领域服务，后续再引入更丰富的上下文与规则编排
        return riskDecisionService.decide(request);
    }

    public void report(RiskReportReq request) {
        riskDecisionService.report(request);
    }

}

