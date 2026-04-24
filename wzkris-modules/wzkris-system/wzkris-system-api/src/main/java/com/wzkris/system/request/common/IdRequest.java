package com.wzkris.system.request.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "主键参数")
public class IdRequest {

    @Schema(description = "主键ID")
    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long id;

}
