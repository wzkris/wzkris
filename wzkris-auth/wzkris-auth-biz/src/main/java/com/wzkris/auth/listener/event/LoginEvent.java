package com.wzkris.auth.listener.event;

import cn.hutool.http.useragent.UserAgent;
import com.wzkris.common.core.domain.CorePrincipal;
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

    private CorePrincipal principal;

    private String grantType;

    private String status;

    private String errorMsg;

    private String ipAddr;

    private UserAgent userAgent;

}
