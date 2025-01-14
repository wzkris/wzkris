package com.wzkris.user.domain.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 角色对用户 一对多
 */
@Data
public class SysRole2UsersReq {

    @NotNull(message = "{desc.role}id{validate.notnull}")
    private Long roleId;

    @NotEmpty(message = "{desc.user}id{validate.notnull}")
    private List<Long> userIds;
}
