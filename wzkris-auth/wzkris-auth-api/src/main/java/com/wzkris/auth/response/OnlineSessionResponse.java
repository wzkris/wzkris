package com.wzkris.auth.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * 在线会话返回体
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class OnlineSessionResponse {

    /**
     * 会话ID
     */
    private String sid;

    /**
     * 是否当前会话
     */
    private Boolean current = false;

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
    private OffsetDateTime loginTime;

}
