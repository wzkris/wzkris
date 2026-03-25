package com.wzkris.system.remote.api.loginlog.request;

import lombok.Data;

import java.util.Date;

@Data
public class LoginLogEventRequest {

    private String authType;

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

    private Date loginTime;

    private String abnormalTags;

    private String riskLevel;

    private Integer riskScore;

}
