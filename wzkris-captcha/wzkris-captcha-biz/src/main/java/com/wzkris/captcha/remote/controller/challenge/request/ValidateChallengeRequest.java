package com.wzkris.captcha.remote.controller.challenge.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValidateChallengeRequest {

    @NotBlank
    private String token;

    public ValidateChallengeRequest(String captchaId) {
        this.token = captchaId;
    }

}


