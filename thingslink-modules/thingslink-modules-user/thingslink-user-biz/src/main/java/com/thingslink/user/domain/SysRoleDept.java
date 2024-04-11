package com.thingslink.user.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 角色和部门关联 sys_role_dept
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleDept {
    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "部门ID")
    private Long deptId;
}
