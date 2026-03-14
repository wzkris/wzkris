package com.wzkris.risk.httpclient.captcha.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CaptchaSmsValidateReq implements Serializable {

    @NotBlank
    private String phone;

    @NotBlank
    private String code;

}
