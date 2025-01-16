package com.wzkris.user.domain.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 用户对角色 一对多
 */
@Data
public class SysUser2RolesReq {

    @NotNull(message = "{desc.user}id{validate.notnull}")
    private Long userId;

    @NotEmpty(message = "{desc.role}id{validate.notnull}")
    private List<Long> roleIds;
}
