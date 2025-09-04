package com.wzkris.auth.domain.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 发送短信请求体
 *
 * @author wzkris
 */
@Data
public class SmsCodeReq {

    @NotBlank(message = "{invalidParameter.captcha.error}")
    @Length(min = 1, max = 100, message = "{invalidParameter.captcha.error}")
    private String captchaId;

    @NotBlank(message = "{invalidParameter.phonenumber.invalid}")
    @Length(min = 11, max = 11, message = "{invalidParameter.phonenumber.invalid}")
    private String phone;

}
