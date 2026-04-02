package com.wzkris.system.response.tenantlog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class TenantLoginLogInfoResponse {

    private Long logId;

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
    private Date loginTime;

    @Schema(description = "异常标签")
    private String abnormalTags;

    @Schema(description = "风险等级")
    private String riskLevel;

    @Schema(description = "风险分")
    private Integer riskScore;

}
