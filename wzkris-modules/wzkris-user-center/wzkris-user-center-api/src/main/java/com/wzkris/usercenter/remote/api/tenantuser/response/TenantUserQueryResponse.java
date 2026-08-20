package com.wzkris.usercenter.remote.api.tenantuser.response;

import com.wzkris.usercenter.enums.tenantuser.TenantUserStatusEnum;
import com.wzkris.usercenter.enums.tenant.TenantStatusEnum;
import com.wzkris.usercenter.enums.tenantpackage.TenantPackageStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
public class TenantUserQueryResponse implements Serializable {

    private Long id;

    private Long tenantId;

    private String username;

    private String phoneNumber;

    private String socialUid;

    private TenantUserStatusEnum status;

    private String password;

    private TenantStatusEnum tenantStatus;

    private OffsetDateTime tenantExpired;

    private TenantPackageStatusEnum packageStatus;

}
