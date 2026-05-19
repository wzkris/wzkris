package com.wzkris.auth.api.online.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "会话参数")
public class SidRequest {

    @Schema(description = "会话ID")
    @NotBlank(message = "{invalidParameter.param.invalid}")
    private String sid;

}
