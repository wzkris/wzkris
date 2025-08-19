package com.wzkris.user.domain.req;

import com.wzkris.common.validator.annotation.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 修改手机号
 */
@Data
public class EditPhoneReq {

    @PhoneNumber
    @NotBlank(message = "{invalidParameter.phonenumber.invalid}")
    private String phoneNumber;

    @NotBlank(message = "{invalidParameter.captcha.error}")
    private String smsCode;

}
