package com.wzkris.risk.httpclientimpl.riskctl;

import com.wzkris.common.core.model.Result;
import com.wzkris.risk.domain.RiskDecision;
import com.wzkris.risk.httpclient.riskctl.RiskClient;
import com.wzkris.risk.httpclient.riskctl.req.RiskDecideReq;
import com.wzkris.risk.httpclient.riskctl.req.RiskReportReq;
import com.wzkris.risk.httpclient.riskctl.resp.RiskDecisionResp;
import com.wzkris.risk.service.RiskDecisionService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@Validated
@RestController
@RequiredArgsConstructor
public class RiskDecisionClientImpl implements RiskClient {

    private final RiskDecisionService riskDecisionService;

    @Override
    public Result<RiskDecisionResp> decide(@Valid RiskDecideReq request) {
        RiskDecision decide = riskDecisionService.decide(request);
        RiskDecisionResp resp = new RiskDecisionResp();
        BeanUtils.copyProperties(decide, resp);
        return Result.ok(resp);
    }

    @Override
    public Result<Void> report(@Valid RiskReportReq request) {
        riskDecisionService.report(request);
        return Result.ok();
    }

}
