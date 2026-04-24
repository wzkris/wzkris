package com.wzkris.usercenter.request.role;

import com.wzkris.usercenter.enums.role.RoleStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

/**
 * 新增角色请求体
 */
@Data
@Schema(description = "新增角色参数体")
public class RoleMngSaveRequest {

    @Schema(description = "数据范围（1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限）")
    private String dataScope;

    @NotBlank(message = "{invalidParameter.roleName.invalid}")
    @Size(min = 2, max = 20, message = "{invalidParameter.roleName.invalid}")
    @Schema(description = "角色名称")
    private String roleName;

    @NotNull(message = "{invalidParameter.status.invalid}")
    @Schema(description = "状态")
    private RoleStatusEnum status;

    @NotNull(message = "{invalidParameter.sort.invalid}")
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

