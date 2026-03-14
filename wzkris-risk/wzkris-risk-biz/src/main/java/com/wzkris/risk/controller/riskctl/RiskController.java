package com.wzkris.risk.interfaces.riskctl;

import com.wzkris.common.core.model.Result;
import com.wzkris.risk.application.RiskDecisionAppService;
import com.wzkris.risk.domain.RiskDecision;
import com.wzkris.risk.httpclient.riskctl.req.RiskDecideReq;
import com.wzkris.risk.httpclient.riskctl.req.RiskReportReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.wzkris.common.core.model.Result.ok;

@Tag(name = "风控")
@Validated
@RestController
@RequestMapping("/risk")
@RequiredArgsConstructor
public class RiskController {

    private final RiskDecisionAppService riskDecisionAppService;

    @Operation(summary = "风险决策")
    @PostMapping("/decide")
    public Result<RiskDecision> decide(@RequestBody @Valid RiskDecideReq request) {
        return ok(riskDecisionAppService.decide(request));
    }

    @Operation(summary = "风险回传")
    @PostMapping("/report")
    public Result<Void> report(@RequestBody @Valid RiskReportReq request) {
        riskDecisionAppService.report(request);
        return ok();
    }

}
