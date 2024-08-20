package com.wzkris.system.api.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 登录日志
 * @date : 2023/8/26 14:35
 */
@Data
@Accessors(chain = true)
public class LoginLogDTO {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long logId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录状态（0正常 1异常）
     */
    private String status;

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
    private Long loginTime;
}
