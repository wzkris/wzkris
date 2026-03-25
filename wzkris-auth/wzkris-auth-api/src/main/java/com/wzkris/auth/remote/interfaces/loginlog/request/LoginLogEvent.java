package com.wzkris.auth.remote.interfaces.loginlog.request;

import lombok.Data;

import java.util.Date;

/**
 * 登录日志事件
 */
@Data
public class LoginLogEvent {

    /**
     * 认证类型
     */
    private String authType;

    /**
     * 登录用户ID
     */
    private Long operatorId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录类型
     */
    private String loginType;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 登录地址
     */
    private String loginLocation;

    /**
     * 链路追踪ID
     */
    private String traceId;

    /**
     * 原始UA
     */
    private String userAgent;

    /**
     * 登录状态
     */
    private Boolean success;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 异常标签
     */
    private String abnormalTags;

    /**
     * 风险等级（LOW/MEDIUM/HIGH）
     */
    private String riskLevel;

    /**
     * 风险分（0-100）
     */
    private Integer riskScore;

}
