package com.wzkris.usercenter.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "单参数请求体")
public class StringValueRequest {

    @Schema(description = "参数值")
    @NotBlank(message = "{invalidParameter.param.invalid}")
    private String value;

}
