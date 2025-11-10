package com.wzkris.principal.domain.req.tenant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前端查询条件
 */
@Data
@Schema(description = "租户管理查询参数体")
public class TenantMngQueryReq {

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "租户状态")
    private String status;

}
