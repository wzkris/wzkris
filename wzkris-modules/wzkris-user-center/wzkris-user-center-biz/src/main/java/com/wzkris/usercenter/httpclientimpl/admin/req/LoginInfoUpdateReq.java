package com.wzkris.usercenter.httpclientimpl.admin.req;

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
public class LoginInfoUpdateReq implements Serializable {

    private Long id;

    private String loginIp;

    private Date loginDate;

    public LoginInfoUpdateReq(Long id) {
        this.id = id;
    }

}
