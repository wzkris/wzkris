package com.wzkris.user.domain.req;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

/**
 * 用户对角色 一对多
 */
@Data
public class SysUser2RolesReq {

    @NotNull(message = "{desc.user}{desc.id}{validate.notnull}")
    private Long userId;

    private List<Long> roleIds;
}
