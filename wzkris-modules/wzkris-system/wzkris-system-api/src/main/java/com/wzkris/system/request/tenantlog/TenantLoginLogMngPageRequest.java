package com.wzkris.system.request.tenantlog;

import com.wzkris.common.core.model.QueryRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class TenantLoginLogMngPageRequest extends QueryRequest {

    @Schema(description = "用户ID")
    private Long memberId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "登录状态")
    private Boolean success;

    @Schema(description = "登录地址")
    private String loginLocation;

    @Schema(description = "链路追踪ID")
    private String traceId;

    @Schema(description = "风险等级（LOW/MEDIUM/HIGH）")
    private String riskLevel;

    @Schema(description = "异常标签（支持模糊匹配）")
    private String abnormalTag;

    @Schema(description = "仅异常记录（true 时过滤风险等级非 LOW）")
    private Boolean abnormalOnly;

}

