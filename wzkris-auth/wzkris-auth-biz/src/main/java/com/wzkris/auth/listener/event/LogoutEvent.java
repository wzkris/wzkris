package com.wzkris.auth.listener.event;

import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * 退出登录事件
 */
@Getter
@AllArgsConstructor
public class LogoutEvent {

    private Serializable uid;

    private AuthTypeEnum authType;

}
