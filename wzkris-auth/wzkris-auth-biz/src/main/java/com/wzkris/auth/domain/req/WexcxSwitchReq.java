package com.wzkris.auth.domain.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WexcxSwitchReq {

    @NotBlank
    private String wxCode;

}
