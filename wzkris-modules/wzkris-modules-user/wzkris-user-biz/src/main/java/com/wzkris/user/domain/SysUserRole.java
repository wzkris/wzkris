package com.wzkris.user.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户和角色关联 sys_user_role
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SysUserRole {
    @NotNull(message = "[userId] {validate.notnull}")
    @Schema(description = "用户ID")
    private Long userId;

    @NotNull(message = "[roleId] {validate.notnull}")
    @Schema(description = "角色ID")
    private Long roleId;
}
