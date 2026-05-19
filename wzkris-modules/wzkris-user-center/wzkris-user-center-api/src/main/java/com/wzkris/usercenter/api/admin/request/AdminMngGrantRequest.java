package com.wzkris.usercenter.api.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "管理员授权参数体")
public class AdminMngGrantRequest {

    @NotNull(message = "{invalidParameter.id.invalid}")
    @Schema(description = "管理员 ID")
    private Long adminId;

    @Schema(description = "角色 ID 列表")
    private List<Long> roleIds;

}

