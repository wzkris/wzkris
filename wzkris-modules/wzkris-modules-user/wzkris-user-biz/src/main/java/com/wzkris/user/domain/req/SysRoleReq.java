package com.wzkris.user.domain.req;

import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.validator.annotation.EnumsCheck;
import com.wzkris.common.validator.group.ValidationGroups;
import com.wzkris.user.domain.SysRole;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
@AutoMappers({@AutoMapper(target = SysRole.class)})
@Schema(description = "系统角色添加修改参数体")
public class SysRoleReq {

    @NotNull(groups = ValidationGroups.Update.class, message = "{invalidParameter.id.invalid}")
    private Long roleId;

    @Schema(description = "数据范围（1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限）")
    private String dataScope;

    @NotBlank(message = "{invalidParameter.roleName.invalid}")
    @Size(min = 2, max = 20, message = "{invalidParameter.roleName.invalid}")
    @Schema(description = "角色名称")
    private String roleName;

    @NotBlank(message = "{invalidParameter.status.invalid}")
    @EnumsCheck(values = {CommonConstants.STATUS_DISABLE, CommonConstants.STATUS_ENABLE},
            message = "{invalidParameter.status.invalid}")
    @Schema(description = "状态（0代表正常 1代表停用）")
    private String status;

    @NotNull(message = "继承关系" + "{validate.notnull}")
    @Schema(description = "继承关系")
    private Boolean inherited;

    @NotNull(message = "{invalidParameter.sort.invalid}")
    @Range(message = "{invalidParameter.sort.invalid}")
    @Schema(description = "角色排序")
    private Integer roleSort;

    @Schema(description = "菜单树选择项是否关联显示")
    private Boolean isMenuDisplay;

    @Schema(description = "部门树选择项是否关联显示")
    private Boolean isDeptDisplay;

    @Schema(description = "菜单组")
    private List<Long> menuIds;

    @Schema(description = "部门组")
    private List<Long> deptIds;

    @Schema(description = "继承角色组")
    private List<Long> inheritedIds;

}
