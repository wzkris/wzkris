package com.wzkris.equipment.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author wzkris
 */
@Data
@Schema(description = "修改状态请求体")
public class EditStatusReq {
    @NotNull(message = "[id] {validate.notnull}")
    @Schema(description = "主键")
    private Long id;

    @NotBlank(message = "[status] {validate.notnull}")
    @Schema(description = "状态值")
    private String status;
}
