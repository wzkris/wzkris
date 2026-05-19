package com.wzkris.usercenter.api.admin.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "管理员角色选择参数")
public class AdminMngRoleSelectRequest {

    @Parameter(description = "管理员ID")
    private Long adminId;

    @Parameter(description = "角色名称")
    private String roleName;

}
