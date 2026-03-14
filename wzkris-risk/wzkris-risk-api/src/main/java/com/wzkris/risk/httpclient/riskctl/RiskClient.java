package com.wzkris.risk.httpclient.riskctl;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.httpclient.annotation.HttpClient;
import com.wzkris.common.httpclient.constants.ServiceContextPathConstant;
import com.wzkris.common.httpclient.constants.ServiceIdConstant;
import com.wzkris.risk.httpclient.riskctl.req.RiskDecideReq;
import com.wzkris.risk.httpclient.riskctl.req.RiskReportReq;
import com.wzkris.risk.httpclient.riskctl.resp.RiskDecisionResp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpClient(
        serviceId = ServiceIdConstant.RISK,
        path = ServiceContextPathConstant.RISK
)
@HttpExchange(url = "/risk-client")
public interface RiskClient {

    @PostExchange("/decide")
    Result<RiskDecisionResp> decide(@RequestBody RiskDecideReq request);

    @PostExchange("/report")
    Result<Void> report(@RequestBody RiskReportReq request);

}
