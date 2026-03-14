package com.wzkris.risk.httpclient.riskctl.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class RiskReportReq implements Serializable {

    @NotBlank
    private String scenario;

    @NotBlank
    private String authType;

    @NotBlank
    private String subject;

    @NotNull
    private Boolean success;

    private String ipAddr;

    private String userAgent;

    private String traceId;

}
