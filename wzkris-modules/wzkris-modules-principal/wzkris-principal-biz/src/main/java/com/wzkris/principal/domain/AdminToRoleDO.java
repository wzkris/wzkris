package com.wzkris.principal.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理员和角色关联
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminToRoleDO {

    @Schema(description = "用户ID")
    private Long adminId;

    @Schema(description = "角色ID")
    private Long roleId;

}
