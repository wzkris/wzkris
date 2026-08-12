package com.wzkris.usercenter.api.tenantuser.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户角色选择参数")
public class TenantUserMngRoleSelectRequest {

    @Parameter(description = "用户ID")
    private Long id;

    @Parameter(description = "角色名称")
    private String roleName;

}
