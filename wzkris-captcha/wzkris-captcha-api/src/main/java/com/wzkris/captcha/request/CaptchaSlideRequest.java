package com.wzkris.captcha.request;

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

