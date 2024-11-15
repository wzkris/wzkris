package com.wzkris.user.domain.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 修改手机号
 */
@Data
public class EditPhoneReq {
    @NotBlank(message = "[phoneNumber] {validate.notnull}")
    private String phoneNumber;

    @NotBlank(message = "[smsCode] {validate.notnull}")
    private String smsCode;
}
