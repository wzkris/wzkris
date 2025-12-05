package com.wzkris.message.httpservice.loginlog.req;

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
     * 登录状态
     */
    private Boolean success;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录时间
     */
    private Date loginTime;

}

