package com.wzkris.auth.httpclient.captchachallenge.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValidateChallengeReq {

    @NotBlank
    private String token;

    public ValidateChallengeReq(String captchaId) {
        this.token = captchaId;
    }

}
