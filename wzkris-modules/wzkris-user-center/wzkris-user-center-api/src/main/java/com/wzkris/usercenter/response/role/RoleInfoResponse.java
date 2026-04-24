package com.wzkris.usercenter.response.role;

import com.wzkris.usercenter.enums.role.RoleStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleInfoResponse {

    private Long roleId;

    @Schema(description = "数据范围（1=所有数据权限，2=自定义数据权限，3=本部门数据权限，4=本部门及以下数据权限，5=仅本人数据权限）")
    private String dataScope;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "状态")
    private RoleStatusEnum status;

    @Schema(description = "角色排序")
    private Integer roleSort;

}
