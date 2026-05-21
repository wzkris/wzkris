package com.wzkris.gateway.remote.api.risk.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RiskPassExchangeRequest {

    /**
     * challenge 流程 redeem 成功后返回的校验 token（与登录 captcha_id 同源，二选一消耗）。
     */
    @NotBlank(message = "invalidParameter.captcha.error")
    private String captchaToken;

    /**
     * 验证码形态，与 captcha 服务 CaptchaTypeEnum 的 value 一致（如 CHALLENGE、SLIDE、IMAGE）。
     */
    @NotBlank(message = "invalidParameter.captcha.error")
    private String captchaType;

}
