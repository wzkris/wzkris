package com.wzkris.usercenter.domain.req.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "角色管理查询参数体")
public class RoleMngQueryReq {

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "状态（0代表正常 1代表停用）")
    private String status;

}
