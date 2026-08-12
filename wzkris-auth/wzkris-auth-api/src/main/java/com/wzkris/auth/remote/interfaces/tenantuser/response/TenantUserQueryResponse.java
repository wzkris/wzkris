package com.wzkris.auth.remote.interfaces.tenantuser.response;

import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * sys用户信息
 *
 * @author wzkris
 */
@Data
public class TenantUserQueryResponse implements Serializable {

    private Long id;

    private Long tenantId;

    private String username;

    private String phoneNumber;

    private String status;

    private String password;

    private String tenantStatus;

    private OffsetDateTime tenantExpired;

    private String packageStatus;

}

