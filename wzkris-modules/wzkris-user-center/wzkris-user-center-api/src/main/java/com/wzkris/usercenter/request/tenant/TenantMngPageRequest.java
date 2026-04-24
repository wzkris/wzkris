package com.wzkris.usercenter.request.tenant;

import com.wzkris.usercenter.enums.tenant.TenantStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前端查询条件
 */
@Data
@Schema(description = "租户管理查询参数体")
public class TenantMngPageRequest {

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "租户状态")
    private TenantStatusEnum status;

}

