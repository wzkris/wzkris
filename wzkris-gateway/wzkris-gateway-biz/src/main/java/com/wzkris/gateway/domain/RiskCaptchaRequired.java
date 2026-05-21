package com.wzkris.gateway.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskCaptchaRequired implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "是否需要风控验证码")
    private boolean riskCaptchaRequired;

    @Schema(description = "是否处于风控锁定")
    private boolean riskLocked;

}
