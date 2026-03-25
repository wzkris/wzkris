package com.wzkris.captcha.remote.controller.image.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ImageCaptchaRequest {

    @NotNull(message = "token不能为空")
    private String token;

    @NotNull(message = "验证码不能为空")
    private String code;

}


