package com.wzkris.captcha.request;

import com.wzkris.captcha.enums.CaptchaTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RiskPassExchangeRequest {

    /**
     * challenge 流程 redeem 成功后返回的校验 token（与登录 captcha_id 同源，二选一消耗）。
     */
    @NotBlank(message = "invalidParameter.captcha.error")
    private String captchaToken;

    /**
     * 验证码形态
     */
    @NotNull(message = "invalidParameter.captcha.error")
    private CaptchaTypeEnum captchaType;

}
