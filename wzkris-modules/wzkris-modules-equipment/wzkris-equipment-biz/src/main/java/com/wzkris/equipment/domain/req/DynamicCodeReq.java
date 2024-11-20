package com.wzkris.equipment.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author wzkris
 */
@Data
@Schema(description = "代码编译参数体")
public class DynamicCodeReq {
    @NotBlank(message = "脚本代码块不能为空")
    @Schema(description = "待编译代码")
    private String code;

    @NotBlank(message = "代码块入参不能为空")
    @Schema(description = "入参")
    private String inparam;
}
