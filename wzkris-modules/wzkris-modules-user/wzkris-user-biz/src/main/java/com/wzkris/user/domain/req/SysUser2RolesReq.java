package com.wzkris.user.domain.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 用户对角色 一对多
 */
@Data
public class SysUser2RolesReq {

    @NotNull(message = "{desc.user}{desc.id}{validate.notnull}")
    private Long userId;

    private List<Long> roleIds;
}
