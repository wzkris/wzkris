package com.wzkris.captcha.api.captcha.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CaptchaSlideRequest {

    @NotBlank
    private String token;

    @NotNull
    private Integer x;

}

