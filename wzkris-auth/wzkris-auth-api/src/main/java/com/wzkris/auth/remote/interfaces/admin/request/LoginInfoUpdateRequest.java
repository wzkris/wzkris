package com.wzkris.auth.remote.interfaces.admin.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 登录信息
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class LoginInfoUpdateRequest implements Serializable {

    private Long id;

    private String loginIp;

    private OffsetDateTime loginDate;

    public LoginInfoUpdateRequest(Long id) {
        this.id = id;
    }

}

