package com.wzkris.auth.remote.captcha.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CaptchaCheckReq implements Serializable {

    @NotBlank(message = "验证码key不能为空")
    private String key;

    @NotBlank(message = "验证码value不能为空")
    private String value;

}
