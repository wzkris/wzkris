package com.wzkris.auth.listener.event;

import cn.hutool.http.useragent.UserAgent;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 用户登出事件
 * @date : 2024/08/05 9:21
 */
@Data
@AllArgsConstructor
public class UserLogoutEvent {
    private String oauth2Type;
    private Object loginer;
    private String ipAddr;
    private UserAgent userAgent;
}
