package com.wzkris.system.api.tenantlog.login.response;

import com.wzkris.common.core.enums.RiskLevelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class TenantLoginLogMngResponse {

    private Long logId;

    @Schema(description = "用户ID")
    private Long memberId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "登录类型")
    private String loginType;

    @Schema(description = "登录ip")
    private String loginIp;

    @Schema(description = "登录地址")
    private String loginLocation;

    @Schema(description = "链路追踪ID")
    private String traceId;

    @Schema(description = "原始UA")
    private String userAgent;

    @Schema(description = "登录状态")
    private Boolean success;

    @Schema(description = "失败信息")
    private String errorMsg;

    @Schema(description = "登录时间")
    private OffsetDateTime loginTime;

    @Schema(description = "异常标签")
    private String abnormalTags;

    @Schema(description = "风险等级")
    private RiskLevelEnum riskLevel;

    @Schema(description = "风险分")
    private Integer riskScore;

    @Schema(description = "租户ID")
    private Long tenantId;

}
