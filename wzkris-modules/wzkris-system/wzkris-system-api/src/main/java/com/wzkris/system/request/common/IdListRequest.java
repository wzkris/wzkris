package com.wzkris.system.request.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "主键列表参数")
public class IdListRequest {

    @Schema(description = "主键ID列表")
    @NotEmpty(message = "{invalidParameter.id.invalid}")
    private List<Long> ids;

}
