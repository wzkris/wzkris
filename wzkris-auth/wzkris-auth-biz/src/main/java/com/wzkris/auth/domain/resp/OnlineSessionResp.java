package com.wzkris.auth.domain.resp;

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
public class OnlineSessionResp extends OnlineSession {

    /**
     * token
     */
    private String refreshToken;

    /**
     * 是否当前会话
     */
    private Boolean current = false;

    public OnlineSessionResp(OnlineSession onlineSession) {
        setDeviceType(onlineSession.getDeviceType());
        setLoginIp(onlineSession.getLoginIp());
        setLoginLocation(onlineSession.getLoginLocation());
        setBrowser(onlineSession.getBrowser());
        setOs(onlineSession.getOs());
        setLoginTime(onlineSession.getLoginTime());
    }

}
