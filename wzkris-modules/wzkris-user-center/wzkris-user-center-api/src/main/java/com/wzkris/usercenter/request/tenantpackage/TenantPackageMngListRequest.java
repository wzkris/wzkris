package com.wzkris.usercenter.request.tenantpackage;

import com.wzkris.usercenter.enums.tenantpackage.TenantPackageStatusEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "租户套餐管理查询参数体")
public class TenantPackageMngListRequest {

    @Parameter(description = "套餐名称")
    private String packageName;

    @Parameter(description = "状态（0正常 1停用）")
    private TenantPackageStatusEnum status;
}
