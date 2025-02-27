package com.wzkris.user.domain.req;

import com.wzkris.common.core.annotation.EnumsCheck;
import com.wzkris.common.core.annotation.group.ValidationGroups;
import com.wzkris.common.core.constant.CommonConstants;
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

    @NotNull(groups = ValidationGroups.Update.class, message = "id {validate.notnull}")
    private Long roleId;

    @Schema(description = "数据范围（1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限）")
    private String dataScope;

    @NotBlank(message = "{desc.role}{desc.name}" + "{validate.notnull}")
    @Size(min = 2, max = 20, message = "{desc.role}{desc.name}" + "{validate.size.illegal}")
    @Schema(description = "角色名称")
    private String roleName;

    @EnumsCheck(values = {CommonConstants.STATUS_DISABLE, CommonConstants.STATUS_ENABLE})
    @Schema(description = "状态（0代表正常 1代表停用）")
    private String status;

    @NotNull(message = "{desc.sort}" + "{validate.notnull}")
    @Range(message = "{desc.sort}" + "{validate.size.illegal}")
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
}
