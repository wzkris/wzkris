package com.wzkris.auth.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 在线会话
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class OnlineSession implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 设备
     */
    private String device;

    /**
     * 设备品牌
     */
    private String deviceBrand;

    /**
     * 登录IP地址
     */
    private String loginIp;

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
