package com.wzkris.usercenter.remote.api.operatelog.request;

import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class OperateLogEventRequest {

    private String title;

    private String subTitle;

    private String operType;

    private String method;

    private String httpMethod;

    private AuthTypeEnum authType;

    private Long operatorId;

    private String operName;

    private String httpUrl;

    private String operIp;

    private String operParam;

    private String jsonResult;

    private String operLocation;

    private Boolean success;

    private String errorMsg;

    private OffsetDateTime operTime;

    private Long costTime;

    private Long tenantId;

    private String traceId;

}
