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

    @NotBlank(message = "{captcha.notnull}")
    @Length(min = 32, max = 32, message = "{captcha.error}")
    private String uuid;

    @NotBlank(message = "{captcha.notnull}")
    @Length(min = 1, max = 8, message = "{captcha.error}")
    private String code;

    @NotBlank(message = "{desc.phonenumber}{validate.notnull}")
    @Length(min = 11, max = 11, message = "{desc.phonenumber}{desc.error}")
    private String phone;

}
