package com.wzkris.auth.domain.resp;

import com.wzkris.auth.domain.OnlineUser;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 在线会话返回体
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class OnlineUserResp extends OnlineUser {

    /**
     * token
     */
    private String refreshToken;

    /**
     * 是否当前会话
     */
    private Boolean current = false;

    public OnlineUserResp(OnlineUser onlineUser) {
        setDeviceType(onlineUser.getDeviceType());
        setLoginIp(onlineUser.getLoginIp());
        setLoginLocation(onlineUser.getLoginLocation());
        setBrowser(onlineUser.getBrowser());
        setOs(onlineUser.getOs());
        setLoginTime(onlineUser.getLoginTime());
    }

}
