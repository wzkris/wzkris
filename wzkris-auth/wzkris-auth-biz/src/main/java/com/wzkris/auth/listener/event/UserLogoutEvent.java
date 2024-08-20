package com.wzkris.auth.listener.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 用户登出事件
 * @date : 2024/08/05 9:21
 */
@Getter
@AllArgsConstructor
public class UserLogoutEvent {
    private String oauth2Type;
    private Object loginer;
}
