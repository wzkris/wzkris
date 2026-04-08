package com.wzkris.system.request.tenantlog;

import com.wzkris.common.core.model.QueryRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "租户个人操作日志筛选条件（info）")
public class TenantOperateLogInfoQueryRequest extends QueryRequest {

    @Schema(description = "操作模块")
    private String title;

    @Schema(description = "子模块")
    private String subTitle;

    @Schema(description = "操作类型")
    private String operType;

    @Schema(description = "操作人员")
    private String username;

    @Schema(description = "操作状态")
    private Boolean success;

}
