package com.wzkris.user.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author wzkris
 * @version V1.0.0
 * @description 角色对多用户传输层
 * @date 2024/6/22 09:49
 */
@Data
public class SysRoleUsersDTO {
    @NotNull(message = "[roleId] {validate.notnull}")
    private Long roleId;

    @NotEmpty(message = "[userIds] {validate.notnull}")
    private List<Long> userIds;
}
