package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 角色表 sys_role
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class SysRole extends BaseEntity {

    @TableId
    private Long roleId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "数据范围（1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限）")
    private String dataScope;

    @NotBlank(message = "[roleName] {validate.notnull}")
    @Size(min = 0, max = 30, message = "[roleName] {validate.size.illegal}")
    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "状态（0代表正常 1代表停用）")
    private String status;

    @NotNull(message = "[roleSort] {validate.notnull}")
    @Schema(description = "角色排序")
    private Integer roleSort;

    @Schema(description = "菜单树选择项是否关联显示")
    private Boolean isMenuDisplay;

    @Schema(description = "部门树选择项是否关联显示")
    private Boolean isDeptDisplay;

    public SysRole(Long roleId) {
        this.roleId = roleId;
    }

    public SysRole(String status) {
        this.status = status;
    }
}
