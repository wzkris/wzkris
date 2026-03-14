package com.wzkris.risk.httpclient.riskctl.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class RiskDecideReq implements Serializable {

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

}
