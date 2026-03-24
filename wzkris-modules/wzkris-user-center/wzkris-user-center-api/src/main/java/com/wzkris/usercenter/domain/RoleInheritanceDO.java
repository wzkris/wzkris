package com.wzkris.usercenter.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色继承关系关联表 role_inheritance
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleInheritanceDO {

    @Schema(description = "角色 ID")
    private Long roleId;

    @Schema(description = "子角色 ID")
    private Long childId;

}
