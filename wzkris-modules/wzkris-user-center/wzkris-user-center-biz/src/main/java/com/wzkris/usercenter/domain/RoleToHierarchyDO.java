package com.wzkris.usercenter.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色继承表 role_to_hierarchy
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleToHierarchyDO {

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "继承角色id")
    private Long inheritedId;

}
