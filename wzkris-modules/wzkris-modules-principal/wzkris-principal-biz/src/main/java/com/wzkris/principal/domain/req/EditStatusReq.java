package com.wzkris.principal.domain.req;

import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.validator.annotation.EnumsCheck;
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

    @NotNull(message = "{invalidParameter.id.invalid}")
    @Schema(description = "主键")
    private Long id;

    @EnumsCheck(values = {CommonConstants.STATUS_ENABLE, CommonConstants.STATUS_DISABLE},
            message = "{invalidParameter.status.invalid}")
    @NotBlank(message = "{invalidParameter.status.invalid}")
    @Schema(description = "状态值")
    private String status;

}
