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

    @NotBlank(message = "{captcha.error}")
    @Length(min = 1, max = 100, message = "{captcha.error}")
    private String captchaId;

    @NotBlank(message = "{desc.phonenumber}{validate.notnull}")
    @Length(min = 11, max = 11, message = "{desc.phonenumber}{desc.error}")
    private String phone;

}
