package com.wzkris.principal.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色和菜单关联 role_to_menu
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleToMenuDO {

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "菜单ID")
    private Long menuId;

}
