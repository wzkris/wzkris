package com.wzkris.auth.remote.interfaces.common.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StringValueRequest implements Serializable {

    @Schema(description = "参数值")
    @NotBlank(message = "{invalidParameter.param.invalid}")
    private String value;

}
