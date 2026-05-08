package com.wzkris.usercenter.response.role;

import com.wzkris.usercenter.enums.role.DataScopeEnum;
import com.wzkris.usercenter.enums.role.RoleStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleMngResponse {

    private Long roleId;

    @Schema(description = "数据范围")
    private DataScopeEnum dataScope;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "状态")
    private RoleStatusEnum status;

    @Schema(description = "角色排序")
    private Integer roleSort;

}
