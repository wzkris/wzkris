package com.thingslink.auth.listening.event;

import cn.hutool.http.useragent.UserAgent;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 用户登录事件
 * @date : 2024/2/21 9:09
 */
@Data
@AllArgsConstructor
public class UserLoginEvent {
    private String oauth2Type;
    private Object userinfo;
    private String ip;
    private UserAgent userAgent;
}
