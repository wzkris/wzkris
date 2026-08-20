package com.wzkris.usercenter.api.tenantrole.request;

import com.wzkris.usercenter.enums.tenantrole.TenantRoleStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
@Schema(description = "修改角色参数体")
public class TenantRoleMngUpdateRequest {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long id;

    @Size(min = 2, max = 20, message = "{invalidParameter.roleName.invalid}")
    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "状态")
    private TenantRoleStatusEnum status;

    @Range(message = "{invalidParameter.sort.invalid}")
    @Schema(description = "角色排序")
    private Integer roleSort;

    @Schema(description = "菜单组")
    private List<Long> menuIds;

}

