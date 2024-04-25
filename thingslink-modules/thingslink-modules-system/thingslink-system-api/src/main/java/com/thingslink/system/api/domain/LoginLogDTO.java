package com.thingslink.system.api.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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
     * 用户id
     */
    private Long userId;

    /**
     * 地址
     */
    private String address;

    /**
     * ip地址
     */
    private String ipAddr;

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
