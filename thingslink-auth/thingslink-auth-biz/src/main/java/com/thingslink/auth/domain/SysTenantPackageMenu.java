package com.thingslink.auth.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * 租户套餐关联菜单表 sys_tenant_package_menu
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SysTenantPackageMenu {

    @Schema(description = "租户套餐ID")
    private Long packageId;

    @Schema(description = "菜单ID")
    private Long menuId;
}
