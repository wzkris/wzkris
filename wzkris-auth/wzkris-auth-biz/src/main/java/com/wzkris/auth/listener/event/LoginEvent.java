package com.wzkris.auth.listener.event;

import com.wzkris.common.core.model.MyPrincipal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.basjes.parse.useragent.UserAgent;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 用户登录事件
 * @date : 2024/2/21 9:09
 */
@Getter
@AllArgsConstructor
public class LoginEvent {

    private MyPrincipal principal;

    private String refreshToken;

    private String loginType;

    private Boolean success;

    private String errorMsg;

    private String ipAddr;

    private UserAgent userAgent;

}
