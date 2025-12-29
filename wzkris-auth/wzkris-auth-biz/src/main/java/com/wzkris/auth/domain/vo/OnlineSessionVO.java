package com.wzkris.auth.domain.vo;

import com.wzkris.auth.domain.OnlineSession;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 在线会话返回体
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class OnlineSessionVO extends OnlineSession {

    /**
     * token
     */
    private String refreshToken;

    /**
     * 是否当前会话
     */
    private Boolean current = false;

    public OnlineSessionVO(OnlineSession onlineSession) {
        setDevice(onlineSession.getDevice());
        setDeviceBrand(onlineSession.getDeviceBrand());
        setLoginIp(onlineSession.getLoginIp());
        setBrowser(onlineSession.getBrowser());
        setOs(onlineSession.getOs());
        setLoginTime(onlineSession.getLoginTime());
    }

}
