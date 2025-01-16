package com.wzkris.user.domain.req;

import com.wzkris.common.core.annotation.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 修改手机号
 */
@Data
public class EditPhoneReq {

    @PhoneNumber
    @NotBlank(message = "{desc.phonenumber}" + "{validate.notnull}")
    private String phoneNumber;

    @NotBlank(message = "{desc.smscode}" + "{validate.notnull}")
    private String smsCode;
}
