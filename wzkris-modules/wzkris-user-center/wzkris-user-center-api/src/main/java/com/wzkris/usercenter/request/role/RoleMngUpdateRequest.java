package com.wzkris.usercenter.request.role;

import com.wzkris.usercenter.enums.role.RoleStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

/**
 * 修改角色请求体
 */
@Data
@Schema(description = "修改角色参数体")
public class RoleMngUpdateRequest {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long roleId;

    @Schema(description = "数据范围（1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限）")
    private String dataScope;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "状态")
    private RoleStatusEnum status;

    @Range(message = "{invalidParameter.sort.invalid}")
    @Schema(description = "角色排序")
    private Integer roleSort;

    @Schema(description = "菜单组")
    private List<Long> menuIds;

    @Schema(description = "部门组")
    private List<Long> deptIds;

    @Schema(description = "子角色 ID 列表")
    private List<Long> childIds;

}

