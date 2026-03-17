package com.wzkris.captcha.image.domain.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CaptchaImageReq {

    @NotBlank
    private String token;

    @NotBlank
    private String code;

}
