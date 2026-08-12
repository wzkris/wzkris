package com.wzkris.usercenter.api.tenantuser.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "用户授权角色参数体")
public class TenantUserMngGrantRoleRequest {

    @NotNull(message = "{invalidParameter.id.invalid}")
    @Schema(description = "用户 ID")
    private Long id;

    @Schema(description = "角色 ID 列表")
    private List<Long> tenantRoleIds;

}

