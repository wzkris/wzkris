package com.wzkris.captcha.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CaptchaImageRequest {

    @NotBlank
    private String token;

    @NotBlank
    private String code;

}

