package com.wzkris.system.remote.api.loginlog.request;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.enums.RiskLevelEnum;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class LoginLogEventRequest {

    private AuthTypeEnum authType;

    private Long operatorId;

    private Long tenantId;

    private String username;

    private String loginType;

    private String loginIp;

    private String loginLocation;

    private String traceId;

    private String userAgent;

    private Boolean success;

    private String errorMsg;

    private OffsetDateTime loginTime;

    private String abnormalTags;

    private RiskLevelEnum riskLevel;

    private Integer riskScore;

}
