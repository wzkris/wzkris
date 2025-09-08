package com.wzkris.user.domain.req.role;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 角色对用户 一对多
 */
@Data
public class RoleToUsersReq {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long roleId;

    @NotEmpty(message = "{invalidParameter.id.invalid}")
    private List<Long> userIds;

}
