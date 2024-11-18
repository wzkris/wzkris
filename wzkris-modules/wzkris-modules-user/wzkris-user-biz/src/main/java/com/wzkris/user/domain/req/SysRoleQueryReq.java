package com.wzkris.user.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SysRoleQueryReq {
    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "状态（0代表正常 1代表停用）")
    private String status;
}
