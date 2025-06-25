package com.wzkris.user.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前端查询条件
 */
@Data
public class SysTenantQueryReq {

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "租户状态")
    private String status;

}
