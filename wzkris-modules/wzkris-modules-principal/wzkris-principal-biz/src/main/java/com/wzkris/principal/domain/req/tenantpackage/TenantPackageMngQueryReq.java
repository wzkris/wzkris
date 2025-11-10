package com.wzkris.principal.domain.req.tenantpackage;

import com.wzkris.principal.domain.TenantPackageInfoDO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@AutoMappers({@AutoMapper(target = TenantPackageInfoDO.class)})
@Schema(description = "租户套餐管理查询参数体")
public class TenantPackageMngQueryReq {

    @Schema(description = "套餐名称")
    private String packageName;

    @Schema(description = "状态（0正常 1停用）")
    private String status;

}
