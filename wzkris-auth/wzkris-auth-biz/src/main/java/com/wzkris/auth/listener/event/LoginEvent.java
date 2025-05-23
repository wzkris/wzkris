package com.wzkris.auth.listener.event;

import cn.hutool.http.useragent.UserAgent;
import com.wzkris.common.security.oauth2.domain.AuthBaseUser;
import jakarta.annotation.Nullable;
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

    @Nullable
    private String tokenId;

    private AuthBaseUser user;

    private String grantType;

    private String status;

    private String errorMsg;

    private String ipAddr;

    private UserAgent userAgent;
}
