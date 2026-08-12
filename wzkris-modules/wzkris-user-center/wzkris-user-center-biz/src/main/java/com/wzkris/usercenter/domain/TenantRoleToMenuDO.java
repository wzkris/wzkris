package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 角色和菜单关联表 tenant_role_to_menu
 *
 * @author wzkris
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(schema = "biz", value = "tenant_role_to_menu", autoResultMap = true)
public class TenantRoleToMenuDO extends BaseEntity {

    @Schema(description = "角色ID")
    private Long tenantRoleId;

    @Schema(description = "菜单ID")
    private Long menuId;

}
