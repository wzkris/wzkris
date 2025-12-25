package com.wzkris.auth.httpservice.captcha.req;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

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

    @Nonnull
    private String key;

    @NonNull
    private String code;

}
