package com.wzkris.system.api.tenantlog.request;

import com.wzkris.common.orm.request.PagingRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class TenantOperateLogMngPageRequest extends PagingRequest {

    @Parameter(description = "用户ID")
    private Long memberId;

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
