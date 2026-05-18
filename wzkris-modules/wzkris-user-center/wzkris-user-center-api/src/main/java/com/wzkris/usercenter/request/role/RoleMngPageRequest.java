package com.wzkris.usercenter.request.role;

import com.wzkris.common.orm.request.PagingRequest;
import com.wzkris.usercenter.enums.role.RoleStatusEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "角色管理查询参数体")
public class RoleMngPageRequest extends PagingRequest {

    @Parameter(description = "角色名称")
    private String roleName;

    @Parameter(description = "状态（0代表正常 1代表停用）")
    private RoleStatusEnum status;

}
