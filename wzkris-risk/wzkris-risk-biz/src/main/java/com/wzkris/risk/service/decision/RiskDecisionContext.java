package com.wzkris.risk.service.decision;

import com.wzkris.risk.httpclient.riskctl.req.RiskDecideReq;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RiskDecisionContext {

    @NotBlank
    private String scenario;

    @NotBlank
    private String authType;

    @NotBlank
    private String subject;

    private String captchaId;

    private String ipAddr;

    private String userAgent;

    private String requestPath;

    private String origin;

    private String referer;

    public RiskDecisionContext() {
    }

    /**
     * 通过 HTTP 请求对象构建决策上下文。
     * 后续可以将该构造器迁移到应用层，进一步解耦 DTO 与领域模型。
     */
    public RiskDecisionContext(RiskDecideReq request) {
        this.scenario = request.getScenario();
        this.authType = request.getAuthType();
        this.subject = request.getSubject();
        this.captchaId = request.getCaptchaId();
        this.ipAddr = request.getIpAddr();
        this.userAgent = request.getUserAgent();
        this.requestPath = request.getRequestPath();
        this.origin = request.getOrigin();
        this.referer = request.getReferer();
    }

}
