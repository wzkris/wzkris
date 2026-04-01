package com.wzkris.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WexcxSwitchRequest {

    @NotBlank
    private String wxCode;

}
