package com.wzkris.captcha.remoteimpl.image.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ImageCaptchaReq {

    @NotNull(message = "token不能为空")
    private String token;

    @NotNull(message = "验证码不能为空")
    private String code;

}
