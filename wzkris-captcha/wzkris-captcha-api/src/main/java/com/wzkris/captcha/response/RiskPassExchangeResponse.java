package com.wzkris.captcha.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskPassExchangeResponse {

    private String riskPassToken;

    private OffsetDateTime expiresAt;

    private String captchaType;

    public static RiskPassExchangeResponse of(String riskPassToken, OffsetDateTime expiresAt, String captchaType) {
        return new RiskPassExchangeResponse(riskPassToken, expiresAt, captchaType);
    }

}
