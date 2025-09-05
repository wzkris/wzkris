package com.wzkris.user.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色和部门关联 role_to_dept
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleToDeptDO {

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "部门ID")
    private Long deptId;

}
