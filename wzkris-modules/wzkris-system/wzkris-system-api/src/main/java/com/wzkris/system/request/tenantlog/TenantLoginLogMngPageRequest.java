package com.wzkris.system.request.tenantlog;

import com.wzkris.common.orm.request.PagingRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class TenantLoginLogMngPageRequest extends PagingRequest {

    @Parameter(description = "用户ID")
    private Long memberId;

    @Parameter(description = "用户名")
    private String username;

    @Parameter(description = "登录状态")
    private Boolean success;

    @Parameter(description = "登录地址")
    private String loginLocation;

    @Parameter(description = "链路追踪ID")
    private String traceId;

    @Parameter(description = "风险等级（LOW/MEDIUM/HIGH）")
    private String riskLevel;

    @Parameter(description = "异常标签（支持模糊匹配）")
    private String abnormalTag;

    @Parameter(description = "仅异常记录（true 时过滤风险等级非 LOW）")
    private Boolean abnormalOnly;
}
