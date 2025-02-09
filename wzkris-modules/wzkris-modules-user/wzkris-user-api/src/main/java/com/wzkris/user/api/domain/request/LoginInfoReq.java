package com.wzkris.user.api.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录信息
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class LoginInfoReq implements Serializable {

    private Long userId;

    private String loginIp;

    private Long loginDate;

    public LoginInfoReq(Long userId) {
        this.userId = userId;
    }
}
