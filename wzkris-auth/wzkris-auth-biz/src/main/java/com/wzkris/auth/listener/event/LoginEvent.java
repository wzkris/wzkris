package com.wzkris.auth.listener.event;

import cn.hutool.http.useragent.UserAgent;
import com.wzkris.common.security.oauth2.domain.AuthBaseUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 用户登录事件
 * @date : 2024/2/21 9:09
 */
@Getter
@AllArgsConstructor
public class LoginEvent {

    private AuthBaseUser user;

    private String ipAddr;

    private UserAgent userAgent;
}
