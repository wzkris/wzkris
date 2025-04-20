package com.wzkris.user.domain.req;

import com.wzkris.user.domain.SysTenant;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@AutoMappers({@AutoMapper(target = SysTenant.class)})
@Schema(description = "租户信息修改参数体")
public class SysTenantProfileReq {

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "域名")
    private String domain;
}
