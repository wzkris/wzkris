package com.wzkris.user.rmi.domain.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

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

    private Date loginDate;

    public LoginInfoReq(Long userId) {
        this.userId = userId;
    }

}
