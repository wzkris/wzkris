package com.wzkris.usercenter.api.tenantlog.operate.request;

import com.wzkris.common.orm.request.PagingRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "租户个人操作日志筛选条件（info）")
public class TenantOperateLogInfoPageRequest extends PagingRequest {

    @Parameter(description = "操作模块")
    private String title;

    @Parameter(description = "子模块")
    private String subTitle;

    @Parameter(description = "操作类型")
    private String operType;

    @Parameter(description = "操作人员")
    private String username;

    @Parameter(description = "操作状态")
    private Boolean success;

}
