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

    @NotBlank(message = "请输入验证码")
    @Length(min = 32, max = 32, message = "验证码错误")
    private String uuid;

    @NotBlank(message = "请输入验证码")
    @Length(min = 1, max = 8, message = "验证码错误")
    private String code;

    @NotBlank(message = "请输入手机号")
    @Length(min = 11, max = 11, message = "手机号错误")
    private String phone;

}
