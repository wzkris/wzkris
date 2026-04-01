package com.wzkris.captcha.remote.controller.slide.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SlideCaptchaRequest {

    @NotNull(message = "token不能为空")
    private String token;

    @NotNull(message = "滑动距离不能为空")
    private Integer x;

}


