package com.wzkris.usercenter.api.tenant.request;

import com.wzkris.common.orm.request.PagingRequest;
import com.wzkris.usercenter.enums.tenant.TenantStatusEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "租户管理查询参数体")
public class TenantMngPageRequest extends PagingRequest {

    @Parameter(description = "租户名称")
    private String tenantName;

    @Parameter(description = "租户状态")
    private TenantStatusEnum status;

}
