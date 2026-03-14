package com.wzkris.risk.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskDecision implements Serializable {

    private String decision;

    private String action;

    private String message;

    private String riskLevel;

    private Integer riskScore;

}
