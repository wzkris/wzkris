package com.wzkris.principal.domain.req.role;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 角色对管理员 一对多
 */
@Data
public class RoleToAdminsReq {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long roleId;

    @NotEmpty(message = "{invalidParameter.id.invalid}")
    private List<Long> adminIds;

}
