package com.wzkris.auth.listener.event;

import com.wzkris.common.core.domain.CorePrincipal;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 刷新Token事件
 * @date : 2025/04/26 08:50
 */
@Getter
@AllArgsConstructor
public class RefreshTokenEvent {

    private CorePrincipal principal;

    private String accessToken;

    private String refreshToken;

}
