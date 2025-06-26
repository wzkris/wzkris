package com.wzkris.user.domain.req;

import com.wzkris.user.domain.SysTenantPackage;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@AutoMappers({@AutoMapper(target = SysTenantPackage.class)})
public class SysTenantPackageQueryReq {

    @Schema(description = "套餐名称")
    private String packageName;

    @Schema(description = "状态（0正常 1停用）")
    private String status;

}
