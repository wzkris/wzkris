package com.wzkris.equipment.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author wzkris
 */
@Data
@Schema(description = "绑定参数")
public class BindingReq {

    @NotNull(message = "id {validate.notnull}")
    @Schema(description = "被绑定的对象")
    private Long id;

    @NotEmpty(message = "[bindingIds] {validate.notnull}")
    @Schema(description = "绑定的对象")
    private List<Long> bindingIds;
}
