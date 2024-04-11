package com.thingslink.common.sms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description  : 短信模板
 * @author  : wzkris
 * @version  : V1.0.0
 * @date  : 2022/12/2 16:06
 */
@Getter
@AllArgsConstructor
public enum SmsEnums {

    /**
     * 登录模板
     */
    LOGIN("SMS_216655791", "code"),
    /**
     * 注册模板
     */
    REGISTER("SMS_216655789", "code"),
    /**
     * 修改密码模板
     */
    PASSWORD("SMS_216655788", "code"),
    /**
     * 变更信息验证码
     */
    EDIT("SMS_216655787", "code");

    private final String templateCode;
    private final String paramCodes;
}

