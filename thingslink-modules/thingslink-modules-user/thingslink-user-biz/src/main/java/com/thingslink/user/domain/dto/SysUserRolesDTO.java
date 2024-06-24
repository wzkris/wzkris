package com.thingslink.user.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author wzkris
 * @version V1.0.0
 * @description 用户对多角色传输层
 * @date 2024/6/22 09:49
 */
@Data
public class SysUserRolesDTO {
    @NotNull(message = "[userId] {validate.notnull}")
    private Long userId;

    @NotEmpty(message = "[roleIds] {validate.notnull}")
    private List<Long> roleIds;
}
