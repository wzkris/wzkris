package com.wzkris.gateway.api.risk.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskPassExchangeResponse {

    private String passToken;

    private OffsetDateTime expiresAt;

    /**
     * 与换票时写入 Redis 的形态一致，便于前端确认。
     */
    private String captchaType;

    public static RiskPassExchangeResponse of(String passToken, OffsetDateTime expiresAt, String captchaType) {
        return new RiskPassExchangeResponse(passToken, expiresAt, captchaType);
    }

}
