package com.wzkris.auth.rmi.domain.req;

import com.wzkris.common.core.annotation.PhoneNumber;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 手机验证码dto
 * @date : 2023/4/17 14:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsCheckReq implements Serializable {

    @PhoneNumber
    private String phoneNumber;

    @Length(min = 6, max = 6, message = "{captcha.error}")
    private String smsCode;
}
