package com.wzkris.risk.httpclient.riskctl.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskDecisionResp implements Serializable {

    private String decision;

    private String action;

    private String message;

    private String riskLevel;

    private Integer riskScore;

}
