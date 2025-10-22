package com.wzkris.message.feign.stafflog.req;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class StaffLoginLogReq implements Serializable {

    /**
     * ID
     */
    private Long logId;

    /**
     * 用户ID
     */
    private Long staffId;

    /**
     * 用户名
     */
    private String staffName;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 登录类型
     */
    private String loginType;

    /**
     * 登录状态
     */
    private Boolean success;

    /**
     * 失败信息
     */
    private String errorMsg;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 登录地址
     */
    private String loginLocation;

    /**
     * 浏览器类型
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
