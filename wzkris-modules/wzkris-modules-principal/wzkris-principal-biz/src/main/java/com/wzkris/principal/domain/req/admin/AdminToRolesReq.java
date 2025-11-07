package com.wzkris.principal.domain.req.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 管理员对角色 一对多
 */
@Data
public class AdminToRolesReq {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long adminId;

    private List<Long> roleIds;

}
