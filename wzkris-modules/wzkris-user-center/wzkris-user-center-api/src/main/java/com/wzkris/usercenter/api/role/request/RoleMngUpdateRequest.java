package com.wzkris.usercenter.api.role.request;

import com.wzkris.usercenter.enums.role.DataScopeEnum;
import com.wzkris.usercenter.enums.role.RoleStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
@Schema(description = "修改角色参数体")
public class RoleMngUpdateRequest {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long id;

    @Schema(description = "数据范围")
    private DataScopeEnum dataScope;

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

