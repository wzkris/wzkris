package com.wzkris.captcha.httpclient.challenge.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ValidateChallengeReq {

    @NotBlank
    private String token;

    public ValidateChallengeReq(String captchaId) {
        this.token = captchaId;
    }

}
