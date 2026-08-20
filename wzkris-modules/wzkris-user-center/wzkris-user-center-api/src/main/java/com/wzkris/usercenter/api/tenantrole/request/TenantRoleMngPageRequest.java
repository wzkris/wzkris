package com.wzkris.usercenter.api.tenantrole.request;

import com.wzkris.common.orm.request.PagingRequest;
import com.wzkris.usercenter.enums.tenantrole.TenantRoleStatusEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "角色管理查询参数体")
public class TenantRoleMngPageRequest extends PagingRequest {

    @Parameter(description = "角色名称")
    private String roleName;

    @Parameter(description = "状态（0代表正常 1代表停用）")
    private TenantRoleStatusEnum status;

}
