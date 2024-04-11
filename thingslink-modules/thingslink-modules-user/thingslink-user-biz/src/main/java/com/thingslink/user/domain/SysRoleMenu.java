package com.thingslink.user.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 角色和菜单关联 sys_role_menu
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleMenu {
    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "菜单ID")
    private Long menuId;
}
