package com.wzkris.auth.feign.captcha.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 手机验证码dto
 * @date : 2023/4/17 14:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaCheckReq implements Serializable {

    @NotEmpty(message = "{invalidParameter.captcha.error}")
    private String key;

    @NotEmpty(message = "{invalidParameter.captcha.error}")
    private String code;

}
