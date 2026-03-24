package com.wzkris.captcha.slide.domain.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CaptchaSlideReq {

    @NotBlank
    private String token;

    @NotNull
    private Integer x;

}
