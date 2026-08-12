package com.wzkris.usercenter.api.tenantrole.request;

import com.wzkris.usercenter.enums.tenantrole.TenantRoleStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
@Schema(description = "新增角色参数体")
public class TenantRoleMngSaveRequest {

    @NotBlank(message = "{invalidParameter.roleName.invalid}")
    @Size(min = 2, max = 20, message = "{invalidParameter.roleName.invalid}")
    @Schema(description = "角色名称")
    private String roleName;

    @NotNull(message = "{invalidParameter.status.invalid}")
    @Schema(description = "状态")
    private TenantRoleStatusEnum status;

    @NotNull(message = "{invalidParameter.sort.invalid}")
    @Range(message = "{invalidParameter.sort.invalid}")
    @Schema(description = "角色排序")
    private Integer roleSort;

    @Schema(description = "菜单组")
    private List<Long> menuIds;

}

