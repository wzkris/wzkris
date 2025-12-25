package com.wzkris.usercenter.domain.req;

import com.wzkris.common.core.constant.CommonConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @author wzkris
 */
@Data
@Schema(description = "修改状态请求体")
public class EditStatusReq {

    @NotNull(message = "{invalidParameter.id.invalid}")
    @Schema(description = "主键")
    private Long id;

    @Pattern(
            regexp = "[" +
                    CommonConstants.STATUS_ENABLE +
                    CommonConstants.STATUS_DISABLE
                    + "]",
            message = "{invalidParameter.status.invalid}")
    @NotBlank(message = "{invalidParameter.status.invalid}")
    @Schema(description = "状态值")
    private String status;

}
