package com.wzkris.auth.api.token.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WexcxSwitchRequest {

    @NotBlank
    private String wxCode;

}
