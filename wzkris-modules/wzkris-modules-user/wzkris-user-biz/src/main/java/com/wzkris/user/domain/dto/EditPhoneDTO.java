package com.wzkris.user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditPhoneDTO {
    @NotBlank(message = "[phoneNumber] {validate.notnull}")
    private String phoneNumber;

    @NotBlank(message = "[smsCode] {validate.notnull}")
    private String smsCode;
}
